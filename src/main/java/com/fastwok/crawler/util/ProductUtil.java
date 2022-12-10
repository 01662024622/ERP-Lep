package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.Item;
import com.fastwok.crawler.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductUtil {
    public static List<Product> convert(JSONArray jsonArray,long parentId) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject productObject = jsonArray.getJSONObject(i);
            Product product = convertProduct(productObject,parentId);
            products.add(product);
            if (!productObject.has("products")||productObject.isNull("products")) continue;
            try {

                products.addAll(convert(productObject.getJSONArray("products"),product.getPId()));
            }catch (Exception e){
//                log.info(productObject.toString());
            }
        }
        return products;
    }

    private static Product convertProduct(JSONObject productObject, long parentId) {
        Product product = new Product();
        product.setCode(productObject.getString("sku"));
        product.setPId(productObject.getLong("id"));
        product.setName(productObject.getString("name"));
        product.setParentId(parentId);
        return product;
    }
    public static List<Item> convertItem(JSONArray jsonArray, long orderId) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject itemObject = jsonArray.getJSONObject(i);
            Item item = new Item();
            item.setOrderId(orderId);
            item.setPrice(itemObject.getLong("price"));
            item.setQuantity(itemObject.getLong("total_quantity"));
            item.setPId(itemObject.getLong("id"));
            item.setCode(itemObject.getString("sku"));
            item.setName(itemObject.getString("name"));
            item.setOrderId(orderId);
            items.add(item);
        }
        return items;
    }
}
