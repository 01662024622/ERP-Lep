package com.fastwok.crawler.entities;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="pancake_item_map")
public class PancakeItemMap {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long n_id;
    private String n_parent_code;
    private String n_parent_name;
    private String code;
    private String name;
    private Long inventory;
    private Long price;
    private String p_id;
    private Integer push;
}
