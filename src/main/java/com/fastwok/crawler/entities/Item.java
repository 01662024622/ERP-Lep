package com.fastwok.crawler.entities;

        import lombok.Data;
        import javax.persistence.*;

@Data
@Entity
@Table(name = "Item")
public class Item {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pId; /// ERP ID
    private Long nId; /// ERP ID
    private String code; /// ERP ID
    private String name; /// ERP ID
    private Long quantity; /// ERP ID
    private Long price; /// ERP ID
    private Long OrderId; /// ERP ID
}
