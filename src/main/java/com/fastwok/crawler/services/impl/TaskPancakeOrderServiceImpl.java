package com.fastwok.crawler.services.impl;

import com.fastwok.crawler.entities.*;
import com.fastwok.crawler.repository.*;
import com.fastwok.crawler.services.isservice.TaskPancakeOrderService;
import com.fastwok.crawler.util.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
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
    PancakeItemMapRepository pancakeItemMapRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PancakeStaffRepository pancakeStaffRepository;
    static PancakeToken TokenCode = null;
    private final String N_API = "https://open.nhanh.vn/api/";


    @Override
    public void getData() throws UnirestException, InterruptedException {
        if (TokenCode == null) {
            TokenCode = pancakeTokenRepository.findTopByOrderByIdDesc();
            if (TokenCode == null) {
                return;
            }
        }

        crawlOrders(1, true);
    }

    public void crawlOrders(int page, boolean checkLogin) throws UnirestException, InterruptedException {
        Date date = new Date();
        long from = date.getTime() / 10000 - 3600;
        long to = from + 7200;
        String getOrderUrl = "https://pos.pages.fm/api/v1/shops/268808/orders?" +
                "access_token=" + TokenCode.getToken() +
                "&page_size=100&status=1&updateStatus=inserted_at&editorId=none&option_sort=inserted_at_desc&es_only=true&page=";
        crawlOrder(1, getOrderUrl);
    }

    private void crawlOrder(int page, String body) throws UnirestException, InterruptedException {

        HttpResponse<JsonNode> orders = ApiUtil.GET(body + page);
        JSONObject res = new JSONObject(orders.getBody());
        JSONObject jsonObject = res.getJSONObject("object");
        if (!jsonObject.has("data")) return;
        int page_number = jsonObject.getInt("total_pages");
        JSONArray jsonArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject orderObject = jsonArray.getJSONObject(i);
//            check order created
            long number = pancakeOrderRepository.countByPId(orderObject.getLong("id"));
            if (number > 0) return;
//            convert order
            PancakeOrder order = PancakeOrderUtil.convert(orderObject,pancakeStaffRepository);
//            convert list item
            JSONArray itemsJsonArray = orderObject.getJSONArray("items");
//            check order have item
            if (itemsJsonArray.length() == 0) {
                pancakeOrderRepository.save(order);
                continue;
            }

            List<PancakeItem> items = PancakeItemUtil.convertItem(itemsJsonArray, order.getPId());
            List<PancakeItemMap> pancakeItemMaps = new ArrayList<>();
            List<String> itemQuery = new ArrayList<>();
            for (int x = 0; x < items.size(); x++) {
                PancakeItemMap pancakeItemMap = pancakeItemMapRepository.findFirstByPCode(items.get(x).getCode());
                if (pancakeItemMap == null || pancakeItemMap.getInventory() < items.get(x).getQuantity()) {
                    itemQuery = new ArrayList<>();
                    break;
                } else {
                    items.get(x).setNId(pancakeItemMap.getN_id());
                    pancakeItemMap.setInventory(pancakeItemMap.getInventory() - items.get(x).getQuantity());
                    pancakeItemMaps.add(pancakeItemMap);
                    itemQuery.add(items.get(x).toString());
                }
            }
            if (itemQuery.size() == 0) continue;

            pancakeOrderRepository.save(order);
            pancakeItemRepository.saveAll(items);
            pancakeItemMapRepository.saveAll(pancakeItemMaps);
            String bodyProducts = String.join(",", itemQuery);
            String orderContent = order.toString();
            String bodyCreateOrder = BodyRequest.getBodyGetProduct(orderContent + bodyProducts + "  ]\n" +
                    "}");
            ApiN(N_API + "order/add", bodyCreateOrder);
            String putOrderUrl = "https://pos.pages.fm/api/v1/shops/268808/orders/" + orderObject.getLong("id") +
                    "?access_token=" + TokenCode.getToken();
            orderObject.remove("status");
            orderObject.put("status", 3);
            ApiUtil.PUT(putOrderUrl, "{\"order\":" + orderObject + "}");
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
