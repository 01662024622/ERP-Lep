package com.fastwok.crawler.services.impl;

import com.fastwok.crawler.entities.*;
import com.fastwok.crawler.repository.*;
import com.fastwok.crawler.services.isservice.TaskPancakeOrderService;
import com.fastwok.crawler.util.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
public class TaskPancakeOrderServiceImpl implements TaskPancakeOrderService {
    @Autowired
    PancakeTokenRepository pancakeTokenRepository;
    @Autowired
    PancakeOrderRepository pancakeOrderRepository;
    @Autowired
    PancakeItemRepository pancakeItemRepository;
    @Autowired
    PancakeShopRepository pancakeShopRepository;
    @Autowired
    PancakeInventoryRepository pancakeInventoryRepository;
    @Autowired
    CustomerRepository customerRepository;
    static PancakeToken TokenCode = null;
    private List<PancakeShop> Shops = null;
    private final String P_API = "https://pos.pages.fm/api/v1/shops/";
    private final String N_API = "https://open.nhanh.vn/api/";
    private final String OrderURL = "orders";


    @Override
    public void getData() throws UnirestException, InterruptedException {
        if (TokenCode == null) {
            TokenCode = pancakeTokenRepository.findTopByOrderByIdDesc();
            if (TokenCode == null) {
                return;
            }
        }
        if (Shops == null) {
            Shops = pancakeShopRepository.getPancakeShopByActive();
            if (Shops == null||Shops.isEmpty()) {
                return;
            }
        }


        crawlOrders(1, true);
    }

    public void crawlOrders(int page, boolean checkLogin) throws UnirestException, InterruptedException {
        Date date = new Date();
        long from = date.getTime()/10000-3600;
        long to =from+7200;
        for (int i = 0; i < Shops.size(); i++) {
            String param="/orders?page_size=100&status=3&updateStatus=inserted_at&editorId=none&option_sort=last_updated_order_desc&es_only=true" +
                    "&startDateTime=" +from+
                    "&endDateTime="+to+"&page=";
            crawlOrder(1,P_API+Shops.get(i).getPid()+param);
        }

    }
    private void crawlOrder(int page,String body) throws UnirestException, InterruptedException{

        HttpResponse<JsonNode> orders = ApiUtil.GET(body+page);
        JSONObject res = new JSONObject(orders.getBody());
        JSONObject jsonObject = res.getJSONObject("object");
        if (!jsonObject.has("data")) return;
        int page_number = jsonObject.getInt("page_number");
        JSONArray jsonArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject orderObject = jsonArray.getJSONObject(i);
//            check order created
            long number = pancakeOrderRepository.countByPId(orderObject.getLong("id"));
            if (number > 0) return;
//            convert order
            PancakeOrder order = PancakeOrderUtil.convert(orderObject);
//            convert list item
            JSONArray itemsJsonArray = orderObject.getJSONArray("items");
//            check order have item
            if (itemsJsonArray.length()==0) {
                pancakeOrderRepository.save(order);
                continue;
            }

            List<PancakeItem> items = PancakeItemUtil.convertItem(itemsJsonArray, order.getPId());
            List<String> itemQuery = new ArrayList<>();
            items.forEach((element) -> {
                PancakeInventory itemPancake = pancakeInventoryRepository.findFirstByPId(element.getPId());
                if (itemPancake != null) element.setNId(itemPancake.getNId());
                else {
                    String bodyItemN = BodyRequest.getBodyGetProduct("{\"page\":1,\"name\":\"" + element.getCode().substring(0,element.getCode().length()-1) + "\"}");
                    try {
                        Long idN = getItemIdN(N_API + "product/search", bodyItemN, element.getCode());
                        element.setNId(idN);
                        pancakeItemRepository.save(element);
                    } catch (UnirestException e) {
                        throw new RuntimeException(e);
                    }
                }
                itemQuery.add(element.toString());
            });

            pancakeOrderRepository.save(order);
            String bodyProducts = String.join(",", itemQuery);
            String orderContent = order.toString();
            String bodyCreateOrder = BodyRequest.getBodyGetProduct(orderContent + bodyProducts + "  ]\n" +
                    "}");
            JSONObject jsonObject1 = ApiN(N_API + "order/add", bodyCreateOrder);
//            log.info(jsonObject1.toString());
        }
        if (page_number <= page) {
            return;
        }
        Thread.sleep(10000);
        crawlOrder(page + 1, body);
    }


    private JSONObject ApiN(String url, String body) throws UnirestException {
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

    private Long getItemIdN(String url, String body, String code) throws UnirestException {
        JSONObject jsonObject = ApiN(url, body);
        if (!jsonObject.has("data")) return 37864656L;
        jsonObject = jsonObject.getJSONObject("data");
        if (!jsonObject.has("products")) return 37864656L;
        jsonObject = jsonObject.getJSONObject("products");
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.getJSONObject(key) != null) {
                JSONObject itemObject = jsonObject.getJSONObject(key);
                if (itemObject.getString("code").equalsIgnoreCase(code))
                    return itemObject.getLong("idNhanh");
            }
        }
        keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.getJSONObject(key) != null) {
                JSONObject itemObject = jsonObject.getJSONObject(key);
                if (itemObject.getString("code").replace("-", "").equalsIgnoreCase(code.replace("-", "")))
                    return itemObject.getLong("idNhanh");
            }
        }
        return 37864656L;
    }


}
