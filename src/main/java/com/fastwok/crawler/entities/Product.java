package com.fastwok.crawler.entities;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "products1")
public class Product {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pId; /// ERP ID
    private String code; /// ERP ID
    private String name; /// ERP ID
    private Long parentId; /// ERP ID
}
