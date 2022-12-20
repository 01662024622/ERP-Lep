package com.fastwok.crawler.entities;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "pancake_items")
public class PancakeItem {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pId; /// ERP ID
    private Long nId; /// ERP ID
    private String code; /// ERP ID
    private String name; /// ERP ID
    private Long quantity; /// ERP ID
    private Long price; /// ERP ID
    private Long OrderId; /// ERP ID

    @Override
    public String toString() {
        return "{\n" +
                "      \"id\": " + pId + ",\n" +
                "      \"idNhanh\": " + nId + ",\n" +
                "      \"quantity\": " + quantity + ",\n" +
                "      \"name\":\"" + name + "\",\n" +
                "      \"price\":" + price + "\n" +
                "    }\n";
    }

}
