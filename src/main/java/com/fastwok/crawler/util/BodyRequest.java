package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.Customer;
import com.fastwok.crawler.entities.CustomerNhanh;

public class BodyRequest {
    public static String GetbodyAuth(String user, String password) {
        return "{\n" +
                "    \"username\":\""+user+"\",\n" +
                "    \"password\":\""+password+"\",\n" +
                "    \"service\":\"staff\"\n" +
                "}";
    }
    public static String UpdateAccdoc(String description , String status)
    {
        return "{\n" +
                "    \"username\":\"kt_linhtb\",\n" +
                "    \"password\":\"123456\",\n" +
                "    \"service\":\"staff\"\n" +
                "}";
    }
    public static String getBodyGetProduct(String data) {
        return "appId=73008&version=2.0&businessId=16294&accessToken=MbkK0iZX0IwRewzFLAdJ0w1X9RCEwqmFUJsIpl2kZQ1ytElyOjTmHrjBqHwNKo2ysghCOhbBhzPQ65AVT5GBzFqgBADC2WekYD1jsgQoEtmq9IyrvbVRm8TME0uaDWsV5h1eYq6Jx5Zp87lrHgcZrJtRTsXjAo8uteUbkMgdWPWAt2IMTadyet8H2JTi"
                + "&data="+data;
    }
    public static String updateUser(Customer customer){
        return"{\n" +
                "  \"type\": \"individual\",\n" +
                "  \"name\": \""+customer.getName().replaceAll("\t","").replaceAll("\n","").replaceAll("\"","").trim()+"\",\n" +
                "  \"phone\": \""+customer.getPhone().replaceAll("\t","").replaceAll("\n","").replaceAll("\"","").trim()+"\",\n" +
                "  \"group\": {\n" +
                "    \"id\": 6,\n" +
                "    \"name\": \"MEMBER\"\n" +
                "  },\n" +
                "  \"stores\": [\n" +
                "\n" +
                "  ],\n" +
                "  \"gender\": \"female\",\n" +
                "  \"country\": \"vn\",\n" +
                "  \"permissions\": [\n" +
                "    \"user\"\n" +
                "  ],\n" +
                "  \"total_point\": "+customer.getCoin()+",\n" +
                "  \"status\": \"active\"\n" +
                "}";
    }
    public static String updateUser(CustomerNhanh customer){
        return"{\n" +
                "  \"type\": \"individual\",\n" +
                "  \"name\": \""+customer.getName().replaceAll("\t","").replaceAll("\n","").trim()+"\",\n" +
                "  \"phone\": \""+customer.getPhone().replaceAll("\t","").replaceAll("\n","").trim()+"\",\n" +
                "  \"group\": {\n" +
                "    \"id\": 6,\n" +
                "    \"name\": \"MEMBER\"\n" +
                "  },\n" +
                "  \"stores\": [\n" +
                "\n" +
                "  ],\n" +
                "  \"gender\": \"female\",\n" +
                "  \"country\": \"vn\",\n" +
                "  \"permissions\": [\n" +
                "    \"user\"\n" +
                "  ],\n" +
                "  \"total_point\": "+customer.getCoin()+",\n" +
                "  \"status\": \"active\"\n" +
                "}";
    }
}
