package com.fastwok.crawler.services.impl;

        import com.fastwok.crawler.entities.Product;
        import com.fastwok.crawler.entities.Token;
        import com.fastwok.crawler.repository.ProductRepository;
        import com.fastwok.crawler.repository.TokenRepository;
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
public class TaskOrderServiceImpl implements TaskService {
    @Autowired
    TokenRepository tokenRepository;
    Token TokenCode = null;
    private final String User = "admin";
    private final String Password = "123456a@";
    private final String URL_API = "https://api.lep.vn/v1/";
    private final String N_API = "https://open.nhanh.vn/api/";
    private final String Login = "auth/login-password?group=web";
    private final String Order = "orders";
    private final String ACCDOC = "invoices";
    private int CheckHour = -1;


    @Override
    public void getData() throws UnirestException, InterruptedException {
        TokenCode = tokenRepository.findTopByOrderByIdDesc();
        if(TokenCode == null){
            String body = BodyRequest.GetbodyAuth(User, Password);
            TokenCode = OAuth2(URL_API + Login, body);
            if (TokenCode==null) return;
        }

        crawlOrder(1);
//        crawlAccDoc(today1,today);
//        crawlItem();
    }


    public void crawlOrder(int page) throws UnirestException, InterruptedException {
        Calendar date = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        String today = dateFormat.format(date.getTime());

        date.add(Calendar.HOUR, -1);
        String yesterday = dateFormat.format(date.getTime());

        String paramOrder = "?limit=500&skip=" + (page * 500 - 500) + "&types=order&sources=web,app&stock_id=31&order_by=desc&sort_by=id&statuses=draft&min_created_at="+yesterday+"&max_created_at="+today;
        HttpResponse<JsonNode> orders = Api(URL_API + Order + paramOrder);
        JSONObject res = new JSONObject(orders.getBody());
        JSONObject jsonObject = res.getJSONObject("object");
        if (!jsonObject.has("data")) return;
        int count = jsonObject.getInt("count");
        List<Product> products = ProductUtil.convert(jsonObject.getJSONArray("data"), 0);
        productRepository.saveAll(products);
        if (count < page * 500) {
            log.info("done----------------------");
            return;
        }
        Thread.sleep(10000);
        crawlProduct("", page + 1);
    }
    private Token OAuth2(String url, String body)
            throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.post(url)
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
        JSONObject res = new JSONObject(jsonNodeHttpResponse.getBody());JSONObject jsonObject = res.getJSONObject("object");
        if (!jsonObject.has("data")) return null;
        jsonObject = jsonObject.getJSONObject("data");
        if (!jsonObject.has("token")) return null;
        jsonObject = jsonObject.getJSONObject("token");
        if (!jsonObject.has("access_token")) return null;
        Token token = new Token();
        token.setToken(jsonObject.getString("access_token"));
        tokenRepository.save(token);
        return token;
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
