package com.fastwok.crawler.services.impl;

import com.fastwok.crawler.entities.Item;
import com.fastwok.crawler.entities.Order;
import com.fastwok.crawler.entities.Product;
import com.fastwok.crawler.entities.Token;
import com.fastwok.crawler.repository.ItemRepository;
import com.fastwok.crawler.repository.OrderRepository;
import com.fastwok.crawler.repository.ProductRepository;
import com.fastwok.crawler.repository.TokenRepository;
import com.fastwok.crawler.services.isservice.TaskOrderService;
import com.fastwok.crawler.services.isservice.TaskService;
import com.fastwok.crawler.util.BodyRequest;
import com.fastwok.crawler.util.OrderUtil;
import com.fastwok.crawler.util.ProductUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class TaskOrderServiceImpl implements TaskOrderService {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    static Token TokenCode = null;
    private final String User = "admin";
    private final String Password = "123456a@";
    private final String URL_API = "https://api.lep.vn/v1/";
    private final String N_API = "https://open.nhanh.vn/api/";
    private final String Login = "auth/login-password?group=web";
    private final String OrderURL = "orders";
    private final String ACCDOC = "invoices";
    private int CheckHour = -1;


    @Override
    public void getData() throws UnirestException, InterruptedException {
        if (TokenCode == null) {
            TokenCode = tokenRepository.findTopByOrderByIdDesc();
            if (TokenCode == null) {
                String body = BodyRequest.GetbodyAuth(User, Password);
                TokenCode = OAuth2(URL_API + Login, body);
                if (TokenCode == null) return;
            }
        }


        crawlOrder(1,true);
    }


    public void crawlOrder(int page,boolean checkLogin) throws UnirestException, InterruptedException {
        Calendar date = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        String today = dateFormat.format(date.getTime());

        date.add(Calendar.HOUR, -1);
        String yesterday = dateFormat.format(date.getTime());

        String paramOrder = "?limit=50&skip=" + (page * 50 - 50) + "&types=order&sources=web,app&stock_id=31&order_by=desc&sort_by=id&statuses=draft&min_created_at=" + yesterday + "&max_created_at=" + today;
        HttpResponse<JsonNode> orders = Api(URL_API + OrderURL + paramOrder);
        JSONObject res = new JSONObject(orders.getBody());
        JSONObject jsonObject = res.getJSONObject("object");
        if (!jsonObject.has("data")){
            if (checkLogin){
                String body = BodyRequest.GetbodyAuth(User, Password);
                OAuth2(URL_API + Login, body);
                crawlOrder(page,false);
            }else return;
        }
        int count = jsonObject.getInt("count");
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject orderObject = jsonArray.getJSONObject(i);
            long number = orderRepository.countByPId(orderObject.getLong("id"));
            if (number > 0) return;
            HttpResponse<JsonNode> detail = TaskOrderServiceImpl.Api("https://api.lep.vn/v1/orders/" + orderObject.getLong("id"));
            JSONObject resDetail = new JSONObject(detail.getBody());
            resDetail = resDetail.getJSONObject("object");
            if (!resDetail.has("data")&&checkLogin) continue;
            resDetail = resDetail.getJSONObject("data");
            Order order = OrderUtil.convert(resDetail);
            List<Item> items = ProductUtil.convertItem(resDetail.getJSONArray("products"), order.getPId());
            List<String> itemQuery = new ArrayList<>();
            items.forEach((element)->{
                Item item = itemRepository.findFirstByPId(element.getPId());
                if (item!=null) element.setNId(item.getNId());
                else {
                    String body = BodyRequest.getBodyGetProduct("{\"page\":1,\"name\":\""+element.getCode()+"\"}");
                    try {
                        Long idN = getItemIdN("https://open.nhanh.vn/api/product/search",body,element.getCode());
                        element.setNId(idN);
                    } catch (UnirestException e) {
                        throw new RuntimeException(e);
                    }
                }
                itemQuery.add(element.toString());
            });
            orderRepository.save(order);
            itemRepository.saveAll(items);
            String bodyProducts = String.join(",",itemQuery);
            String body =  BodyRequest.getBodyGetProduct(order +bodyProducts+"  ]\n" +
                    "}");
            JSONObject jsonObject1 = ApiN("https://open.nhanh.vn/api/order/add",body);
            log.info(jsonObject1.toString());
        }
        if (count < page * 50) {
            log.info("done----------------------");
            return;
        }
        Thread.sleep(10000);
        crawlOrder(page + 1,true);
    }

    private Token OAuth2(String url, String body)
            throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.post(url)
                .header("Accept", "*/*")
                .header("x-fw", String.valueOf(timeMilli))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Content-Type", "application/json")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .body(body)
                .asJson();
        JSONObject res = new JSONObject(jsonNodeHttpResponse.getBody());
        JSONObject jsonObject = res.getJSONObject("object");
        if (!jsonObject.has("data")) return null;
        jsonObject = jsonObject.getJSONObject("data");
        if (!jsonObject.has("token")) return null;
        jsonObject = jsonObject.getJSONObject("token");
        if (!jsonObject.has("access_token")) return null;
        Token token = new Token();
        token.setToken(jsonObject.getString("access_token"));
        tokenRepository.save(token);
        return token;
    }
    private JSONObject ApiN(String url,String body)
            throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.post(url)
                .header("Accept", "*/*")
                .header("x-fw", String.valueOf(timeMilli))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .body(body)
                .asJson();
        JSONObject res = new JSONObject(jsonNodeHttpResponse.getBody());
        return res.getJSONObject("object");
    }
    private Long getItemIdN(String url,String body,String code)
            throws UnirestException {
        JSONObject jsonObject =ApiN(url,body);
        if (!jsonObject.has("data")) return 37864656L;
        jsonObject = jsonObject.getJSONObject("data");
        if (!jsonObject.has("products")) return 37864656L;
        jsonObject = jsonObject.getJSONObject("products");
        Iterator<String> keys = jsonObject.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.getJSONObject(key) != null) {
                JSONObject itemObject = jsonObject.getJSONObject(key);
                if(itemObject.getString("code").equalsIgnoreCase(code))
                    return itemObject.getLong("idNhanh");
            }
        }
        keys = jsonObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.getJSONObject(key) != null) {
                JSONObject itemObject = jsonObject.getJSONObject(key);
                if(itemObject.getString("code").replace("-","").equalsIgnoreCase(code.replace("-","")))
                    return itemObject.getLong("idNhanh");
            }
        }
        return 37864656L;
    }
    public static HttpResponse<JsonNode> Api(String url)
            throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        return Unirest.get(url)
                .header("Accept", "*/*")
                .header("Authorization", "Bearer "+TokenCode.getToken().replace("\"",""))
                .header("x-fw", String.valueOf(timeMilli))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Content-Type", "application/json")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .asJson();
    }


}
