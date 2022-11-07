package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "B30AccDocSalesHdr")
public class AccDocHdr {
    @javax.persistence.Id
    private Integer Id;
    private Long idk;
    private String Description;
    private String DocStatus;
}