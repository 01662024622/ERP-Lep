package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.Order;
import com.fastwok.crawler.services.impl.TaskOrderServiceImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;


@Slf4j
public class OrderUtil {
    public static Order convert(JSONObject jsonObject) throws UnirestException {
        Order order = new Order();
        order.setCode(jsonObject.getString("code"));
        order.setPId(jsonObject.getLong("id"));

        JSONObject customerObject = jsonObject.getJSONObject("customer");
        order.setName(customerObject.getString("name"));
        order.setPhone(customerObject.getString("phone"));

        JSONArray deliveries = jsonObject.getJSONArray("deliveries");
        JSONObject delivery = deliveries.getJSONObject(0);
        order.setCity(delivery.getJSONObject("receiver_province").getString("name"));
        order.setDistrict(delivery.getJSONObject("receiver_district").getString("name"));
        order.setWard(delivery.getJSONObject("receiver_ward").getString("name"));
        order.setAddress(delivery.getString("receiver_address"));

        return order;
    }
}
