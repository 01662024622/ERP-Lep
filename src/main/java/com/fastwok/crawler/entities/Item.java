package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="B20Item")
public class Item {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String Code;
    private boolean IsActive;
    private String Kiot_Id;
}
