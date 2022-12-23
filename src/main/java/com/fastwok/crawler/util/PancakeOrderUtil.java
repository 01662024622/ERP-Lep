package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.Order;
import com.fastwok.crawler.entities.PancakeOrder;
import org.json.JSONArray;
import org.json.JSONObject;

public class PancakeOrderUtil {
    public static PancakeOrder convert(JSONObject jsonObject) {
        PancakeOrder order = new PancakeOrder();
        order.setPId(jsonObject.getLong("id"));
        if(jsonObject.has("partner_fee")&&!jsonObject.isNull("partner_fee")){
            order.setTotal_shipping_fee(jsonObject.getLong("partner_fee"));
        }
        if(jsonObject.has("discount")&&!jsonObject.isNull("discount")){
            order.setTotal_shipping_fee(jsonObject.getLong("discount"));
        }

        JSONObject customerObject = jsonObject.getJSONObject("shipping_address");
        order.setName(customerObject.getString("full_name").replaceAll("\t","").replaceAll("\n","").trim());
        order.setPhone(customerObject.getString("phone_number").replaceAll("\\s", "").replaceAll("\t","").replaceAll("\n","").trim());

        order.setCity(customerObject.getString("province_name"));
        order.setDistrict(customerObject.getString("district_name"));
        order.setWard(customerObject.getString("commnue_name"));
        order.setAddress(customerObject.getString("address"));
        return order;
    }
}
