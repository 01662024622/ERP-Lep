package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="B20Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(length = 1000)
    private String ParentId="220241";
    private Integer CustomerType=1;
    private String BranchCode="A01";
    private String KiotViet="kiot tranfer";
    private String Code;
    private String Name;
    private String Address="";
    private String BillingAddress="";
    private String PersonTel="";
    private String Tel="";
    private String Kiot_Id;
}
