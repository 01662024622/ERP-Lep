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
    private String phone;
    private String address;
    private String city;
    private String district;
    private String ward;
}
