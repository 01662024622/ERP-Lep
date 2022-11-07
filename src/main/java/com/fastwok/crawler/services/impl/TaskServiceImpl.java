package com.fastwok.crawler.services.impl;

import com.fastwok.crawler.entities.Product;
import com.fastwok.crawler.repository.ProductRepository;
import com.fastwok.crawler.services.isservice.TaskService;
import com.fastwok.crawler.util.BodyRequest;
import com.fastwok.crawler.util.ProductUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    ProductRepository productRepository;
    String AUTHEN = "";
    private final String USER = "kt_linhtb";
    private final String PASSWORD = "123456";
    private final String URL_API = "https://api.lep.vn/v1/";
    private final String LOGIN = "auth/login-password?group=web";
    private final String SKU = "products";
    private final String ACCDOC = "invoices";
    private int CheckHour = -1;


    @Override
    public void getData() throws UnirestException, InterruptedException {


        Calendar date = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        String today = dateFormat.format(date.getTime());
        int hour = date.get(Calendar.HOUR_OF_DAY);
        date.add(Calendar.HOUR, -20);
        String today1 = dateFormat.format(date.getTime());


        if (CheckHour < hour) {
            String body = BodyRequest.GetbodyAuth(USER, PASSWORD);
            HttpResponse<JsonNode> authed = OAuth2(URL_API + LOGIN, body);
            JSONObject res = new JSONObject(authed.getBody());

            JSONObject jsonObject = res.getJSONObject("object");
            if (!jsonObject.has("data")) return;
            jsonObject = jsonObject.getJSONObject("data");
            if (!jsonObject.has("token")) return;
            jsonObject = jsonObject.getJSONObject("token");
            if (!jsonObject.has("access_token")) return;
            AUTHEN = jsonObject.getString("access_token");
            AUTHEN = "Bearer " + jsonObject.getString("access_token");
            CheckHour = hour + 8;
        }
        crawlProduct(today1, 1);
//        crawlAccDoc(today1,today);
//        crawlItem();
    }


    public void crawlProduct(String today1, int page) throws UnirestException, InterruptedException {
        log.info(page+"---------------");
        String paramCustomer = "?limit=500&skip=" + (page * 500 - 500) + "&types=item&stock_id=31&order_by=desc&sort_by=id";
        HttpResponse<JsonNode> authen = Api(URL_API + SKU + paramCustomer);
        JSONObject res = new JSONObject(authen.getBody());
        JSONObject jsonObject = res.getJSONObject("object");
        if (!jsonObject.has("data")) return;
        int count = jsonObject.getInt("count");
        List<Product> products = ProductUtil.convert(jsonObject.getJSONArray("data"),0);
        productRepository.saveAll(products);
        if (count<page*500) {log.info("done----------------------");return;}
        Thread.sleep(10000);
        crawlProduct("",page+1);
//        if (customers.isEmpty()) return;
//        customers.forEach(customer -> {
//            List<Customer> checkKiotId = customerRepository.findCustomerByKiotId(customer.getKiot_Id());
//            if (checkKiotId.size() > 0) return;
//            Customer checkCode = customerRepository.findCustomerByCode(customer.getCode());
//            if (checkCode != null) {
//                if (checkCode.getPersonTel().equals(customer.getPersonTel())) {
//                    checkCode.setKiot_Id(customer.getKiot_Id());
//                    customer = checkCode;
//                }
//                if (checkCode.getTel().equals(customer.getTel())) {
//                    checkCode.setKiot_Id(customer.getKiot_Id());
//                    customer = checkCode;
//                }
//            }
//            customerRepository.save(customer);
//        });
    }

    //
//    public void crawlAccDoc(String today1, String today) throws UnirestException {
//        String param = "?format=json&fromPurchaseDate=" + today1 + "T00:00:00&toPurchaseDate=" + today + "&orderBy=id&orderDirection=desc&pageSize=100";
//        HttpResponse<JsonNode> authen = Api(URL_API + ACCDOC + param);
//        JSONObject res = new JSONObject(authen.getBody());
//        JSONObject jsonObject = res.getJSONObject("object");
//        if (!jsonObject.has("data")) return;
//        List<AccDoc> accdocs = AccdocUtil.convert(jsonObject.getJSONArray("data"));
//        if (accdocs.isEmpty()) return;
//        AtomicInteger i = new AtomicInteger();
//        accdocs.forEach(accdoc -> {
//            Optional<AccDoc> checkId = accDocRepository.findById(accdoc.getId());
//            if (checkId.isPresent()) return;
//            i.addAndGet(1);
//            accDocRepository.save(accdoc);
//            accDocSaleRepository.saveAll(accdoc.getAccDocSales());
//            accDocRepository.runExec(accdoc.getId());
//            UpdateStatus updateStatus = accDocRepository.updateStatus(accdoc.getId());
//            String updateStatusBody = BodyRequest.UpdateAccdoc(updateStatus.getDescription(), updateStatus.getStatus());
//            try {
//                Put(URL_API + ACCDOC + "/" + updateStatus.getId(), updateStatusBody);
//            } catch (UnirestException e) {
//                e.printStackTrace();
//            }
//        });
//        if (i.get() > 0)
//            accDocRepository.runInventory();
//    }
//
    private HttpResponse<JsonNode> OAuth2(String url, String body)
            throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        return Unirest.post(url)
                .header("Accept", "*/*")
                .header("x-fw", String.valueOf(timeMilli))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Content-Type", "application/json")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .body(body)
                .asJson();
    }

    private HttpResponse<JsonNode> Api(String url)
            throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        return Unirest.get(url)
                .header("Accept", "*/*")
                .header("Authorization", AUTHEN)
                .header("x-fw", String.valueOf(timeMilli))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Content-Type", "application/json")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .asJson();
    }

    private void Put(String url, String body)
            throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        Unirest.put(url)
                .header("Accept", "*/*")
                .header("Authorization", AUTHEN)
                .header("Retailer", "earldom")
                .header("x-fw", String.valueOf(timeMilli))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Content-Type", "application/json")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .body(body)
                .asJson();
    }

}
