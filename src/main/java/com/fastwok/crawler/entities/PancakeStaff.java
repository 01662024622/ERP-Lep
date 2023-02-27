package com.fastwok.crawler.entities;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "pancake_staff")
public class PancakeStaff {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String p_id;
    private Long n_id;
    private String username;
    private String fullname;
}
