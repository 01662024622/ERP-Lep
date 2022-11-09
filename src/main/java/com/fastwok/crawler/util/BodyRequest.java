package com.fastwok.crawler.util;

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
}
