package com.fastwok.crawler.entities;

        import lombok.Data;
        import javax.persistence.*;

@Data
@Entity
@Table(name = "pancake_inventory")
public class PancakeInventory {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pId; /// ERP ID
    private Long nId; /// ERP ID
    private String code; /// ERP ID
    private String name; /// ERP ID

    private Long inventory; /// ERP ID

}
