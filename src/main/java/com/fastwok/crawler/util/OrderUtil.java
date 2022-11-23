package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.Order;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;


@Slf4j
public class OrderUtil {
    public static Order convert(JSONObject jsonObject) {
        Order order = new Order();
        order.setCode(jsonObject.getString("code"));
        order.setPId(jsonObject.getLong("id"));

        JSONObject customerObject = jsonObject.getJSONObject("customer");
        order.setName(customerObject.getString("name"));
        order.setCustomerId(customerObject.getInt("id"));
        order.setPhone(customerObject.getString("phone").replaceAll("\\s", ""));
        if (jsonObject.has("payments")){
            JSONArray payment = jsonObject.getJSONArray("payments");
            if (payment.length()>0){
                if (payment.getJSONObject(0).has("voucher")){
                    order.setCoupon(payment.getJSONObject(0).getJSONObject("voucher").getString("code"));
                }
            }
        }

        JSONArray deliveries = jsonObject.getJSONArray("deliveries");
        JSONObject delivery = deliveries.getJSONObject(0);
        order.setCity(delivery.getJSONObject("receiver_province").getString("name"));
        order.setDistrict(delivery.getJSONObject("receiver_district").getString("name"));
        order.setWard(delivery.getJSONObject("receiver_ward").getString("name"));
        order.setAddress(delivery.getString("receiver_address"));

        return order;
    }
}
