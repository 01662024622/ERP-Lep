package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "B00DocStatus")
public class DocStatus {
    @javax.persistence.Id
    private Integer Id;
    private String DocStatusName;
    private String DocStatusKey;
}