package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.Customer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerUtil {
    public static List<Customer> convert(JSONArray jsonArray){
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject customerJson=jsonArray.getJSONObject(i);
            Customer customer = new Customer();
            customer.setCode(customerJson.getString("code"));
            customer.setKiot_Id(String.valueOf(customerJson.getLong("id")));
            customer.setName(customerJson.getString("name"));
            if (customerJson.has("address")){
                customer.setAddress(customerJson.getString("address"));
                customer.setBillingAddress(customerJson.getString("address"));
            }
            if (customerJson.has("contactNumber")){
                customer.setPersonTel(customerJson.getString("contactNumber"));
                customer.setTel(customerJson.getString("contactNumber"));
            }

            customers.add(customer);
        }
        return customers;
    }
}
