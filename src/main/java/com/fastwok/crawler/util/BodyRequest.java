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
}
