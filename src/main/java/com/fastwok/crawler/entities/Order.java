package com.fastwok.crawler.entities;

        import lombok.Data;
        import javax.persistence.*;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pId;
    private String name;
    private String code;
    private Integer customerId;
    private String phone;
    private String coupon;
    private String address;
    private String city;
    private String district;
    private String ward;
    @Override
    public String toString(){
        return "{\n" +
                "  \"id\": "+pId+",\n" +
                "  \"depotId\":132462,\n" +
                "  \"customerName\": \""+name+"\",\n" +
                "  \"customerMobile\": \""+phone+"\",\n" +
                "  \"customerAddress\": \""+address+"\",\n" +
                "  \"customerCityName\": \""+city+"\",\n" +
                "  \"couponCode\": \""+coupon+"\",\n" +
                "  \"customerDistrictName\": \""+district+"\",\n" +
                "  \"privateDescription\":\"Đơn lên từ website\",\n" +
                "  \"productList\": [\n" ;
    }
}
