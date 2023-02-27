package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.Order;
import com.fastwok.crawler.entities.PancakeOrder;
import com.fastwok.crawler.entities.PancakeStaff;
import com.fastwok.crawler.repository.PancakeStaffRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class PancakeOrderUtil {
    public static PancakeOrder convert(JSONObject jsonObject, PancakeStaffRepository pancakeStaffRepository) {
        PancakeOrder order = new PancakeOrder();
        order.setPId(jsonObject.getLong("id"));
        if (jsonObject.has("partner_fee") && !jsonObject.isNull("partner_fee")) {
            order.setTotal_shipping_fee(jsonObject.getLong("partner_fee"));
        }
        if (jsonObject.has("discount") && !jsonObject.isNull("discount")) {
            order.setMoney_discount(jsonObject.getLong("discount"));
        }

        if (jsonObject.has("creator") && !jsonObject.isNull("creator")) {
            if (jsonObject.getJSONObject("creator").has("fb_id") && !jsonObject.getJSONObject("creator").isNull("fb_id")) {
                if (jsonObject.getJSONObject("creator").getLong("fb_id") > 0) {
                    PancakeStaff pancakeStaff = pancakeStaffRepository.getPancakeStaffByNId(jsonObject.getJSONObject("creator").getLong("fb_id"));
                    if (pancakeStaff != null && pancakeStaff.getN_id() != null) {
                        order.setCreator_id(pancakeStaff.getN_id());
                    }
                }
            }
        }

        JSONObject customerObject = jsonObject.getJSONObject("shipping_address");
        order.setName(customerObject.getString("full_name").replaceAll("\t", "").replaceAll("\n", "").trim());
        order.setPhone(customerObject.getString("phone_number").replaceAll("\\s", "").replaceAll("\t", "").replaceAll("\n", "").trim());

        order.setCity(customerObject.getString("province_name"));
        order.setDistrict(customerObject.getString("district_name"));
        order.setWard(customerObject.getString("commnue_name"));
        order.setAddress(customerObject.getString("address"));
        return order;
    }
}
