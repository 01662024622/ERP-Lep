package com.fastwok.crawler.services.impl;

import com.fastwok.crawler.entities.PancakeItemMap;
import com.fastwok.crawler.entities.PancakeShop;
import com.fastwok.crawler.entities.PancakeToken;
import com.fastwok.crawler.repository.PancakeItemMapRepository;
import com.fastwok.crawler.repository.PancakeTokenRepository;
import com.fastwok.crawler.services.isservice.TaskPushItemPancakeService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TaskPushItemPancakeServiceImpl implements TaskPushItemPancakeService {
    @Autowired
    PancakeTokenRepository pancakeTokenRepository;
    @Autowired
    PancakeItemMapRepository pancakeItemMapRepository;

    static PancakeToken TokenCode = null;
    private List<PancakeShop> Shops = null;
    private final String P_API = "https://pos.pages.fm/api/v1/shops/";
    private final String N_API = "https://open.nhanh.vn/api/";
    private final String OrderURL = "orders";
    private String UrlPushItem = "https://pos.pages.fm/api/v1/shops/268808/products/import?access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiI1MDM1MGMwMy00NGQ5LTQxMWItOTk4OS1jODgyMjA5MjRhOWIiLCJpYXQiOjE2NjcxODQyOTEsImZiX25hbWUiOiJWxakgVGjhuq9uZyIsImZiX2lkIjoiMTQyMTU4NzA2NTI2NTk1IiwiZXhwIjoxNjc0OTYwMjkxfQ.JFxpc_Jxz3SSH7KKIirACH8JYItTkJJ2g9gQIVQVBYA";
    @Override
    public void getData() throws UnirestException, InterruptedException {
        if (TokenCode == null) {
            TokenCode = pancakeTokenRepository.findTopByOrderByIdDesc();
            if (TokenCode == null) {
                return;
            }
        }
        pushItem(0);
    }
    public void pushItem(int page) throws UnirestException, InterruptedException {
        List<PancakeItemMap> pancakeItemMaps = pancakeItemMapRepository.getByPage(20,page*20);
        pancakeItemMaps.forEach(ele->{
            try {
                POST(ele);
                ele.setPush(1);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        });
        pancakeItemMapRepository.saveAll(pancakeItemMaps);
        if (pancakeItemMaps.size() < 20) {
            return;
        }
        Thread.sleep(5000);
        pushItem(page + 1);

    }
    public void POST(PancakeItemMap pancakeItemMap) throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        String size = pancakeItemMap.getCode().substring(pancakeItemMap.getCode().length() - 1);
        if(size!="S"&&size!="M"&&size!="L"){
            size="";
        }else {
            size="size:"+size;
        }
        MultipartBody body= Unirest.post(UrlPushItem)
                .header("Accept", "*/*")
                .header("x-fw", String.valueOf(timeMilli))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .field("params[data][product][new_product_id]", pancakeItemMap.getCode())
                .field("params[data][product][product_name]", pancakeItemMap.getName())
                .field("params[data][variations][0][warehouse_id]", "64c185f4-a7c3-417d-a514-38b624f4a0f2")
                .field("params[data][variations][0][fields]", size)
                .field("params[data][variations][0][weight]", "300")
                .field("params[data][variations][0][retail_price]", pancakeItemMap.getPrice())
                .field("params[data][variations][0][remain_quantity]", pancakeItemMap.getInventory())
                .field("params[is_kiotviet]", "false")
                .field("params[is_auto_gen]", "true")
                .field("params[is_custom_gen]", "false");
        body.asString();
    }
}
