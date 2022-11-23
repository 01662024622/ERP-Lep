package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.Customer;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomerUtil {
    public static List<Customer> convert(JSONArray jsonArray){
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject customerJson=jsonArray.getJSONObject(i);
            Customer customer = new Customer();
            customer.setEId(customerJson.getInt("id"));
            Object aObj = customerJson.get("phone");
            if (aObj instanceof String) {
                customer.setPhone(customerJson.getString("phone"));
            }else {
                continue;
            }
            customer.setName(customerJson.getString("name"));
            customers.add(customer);
        }
        return customers;
    }
}
