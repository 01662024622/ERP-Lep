package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "AbahaAccdocSale")
public class AccDocSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String Kiot_Id;
    @Column(length = 2000)
    private String Description="";
    private Long Quantity;
    private Long Price;
    private Long Discount;
    private Long Total;
    private Long InventoryId;
    private Long BillId;
}
