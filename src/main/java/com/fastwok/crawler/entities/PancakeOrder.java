package com.fastwok.crawler.entities;

import lombok.Data;
import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Data
@Entity
@Table(name = "pancake_orders")
public class PancakeOrder {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pId;
    private String name;
    private String phone;
    private String address;
    private String city;
    private String ward;
    private String district;
    private Long total_shipping_fee;
    private Long money_discount;

    @Override
    public String toString() {
        Calendar date = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String today = dateFormat.format(date.getTime());
        return "{\n" +
                "  \"id\": " + pId + ",\n" +
                "  \"depotId\":133563,\n" +
                "  \"customerName\": \"" + name + "\",\n" +
                "  \"customerMobile\": \"" + phone + "\",\n" +
                "  \"customerAddress\": \"" + address + "\",\n" +
                "  \"customerCityName\": \"" + city + "\",\n" +
                "  \"customerDistrictName\": \"" + district + "\",\n" +
                "  \"customerWardLocationName\": \"" + ward + "\",\n" +
                "  \"createdDateTime\": \"" + today + "\",\n" +
                "  \"privateDescription\":\"Đơn lên từ website\",\n" +
                "  \"customerShipFee\":" + total_shipping_fee + ",\n" +
                "  \"moneyDiscount\":" + money_discount + ",\n" +
                "  \"productList\": [\n";
    }
}