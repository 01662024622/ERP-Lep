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
        if(jsonObject.has("total_coin")&&!jsonObject.isNull("total_coin")){
            order.setCoin(jsonObject.getLong("total_coin"));
        }
        if(jsonObject.has("total_shipping_fee")&&!jsonObject.isNull("total_shipping_fee")){
            order.setTotal_shipping_fee(jsonObject.getLong("total_shipping_fee"));
        }
        if(jsonObject.has("total_unpaid")&&!jsonObject.isNull("total_unpaid")){
            order.setTotal_unpaid(jsonObject.getLong("total_unpaid"));
        }

        JSONObject customerObject = jsonObject.getJSONObject("customer");
        order.setName(customerObject.getString("name").replaceAll("\t","").replaceAll("\n","").trim());
        order.setCustomerId(customerObject.getInt("id"));
        order.setPhone(customerObject.getString("phone").replaceAll("\\s", "").replaceAll("\t","").replaceAll("\n","").trim());
        if (jsonObject.has("payments")){
            JSONArray payment = jsonObject.getJSONArray("payments");
            if (payment.length()>0){
                if (payment.getJSONObject(0).has("voucher")&&!payment.getJSONObject(0).isNull("voucher")){
                    if(!payment.getJSONObject(0).getJSONObject("voucher").isNull("code")&& payment.getJSONObject(0).getJSONObject("voucher").has("code"))
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
