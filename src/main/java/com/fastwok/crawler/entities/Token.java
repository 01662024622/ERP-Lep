package com.fastwok.crawler.entities;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "erp_token")
public class Token {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
}
