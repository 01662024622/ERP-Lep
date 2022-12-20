package com.fastwok.crawler.entities;

        import lombok.Data;
        import javax.persistence.*;

@Data
@Entity
@Table(name = "pancake_shop")
public class PancakeShop {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long Pid;
    private boolean active;
}
