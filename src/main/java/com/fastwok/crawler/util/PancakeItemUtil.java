package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.Item;
import com.fastwok.crawler.entities.PancakeItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PancakeItemUtil {
    public static List<PancakeItem> convertItem(JSONArray jsonArray, long orderId) {
        List<PancakeItem> items = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject itemObject = jsonArray.getJSONObject(i);
            PancakeItem item = new PancakeItem();
            item.setOrderId(orderId);
            item.setQuantity(itemObject.getLong("quantity"));
            item.setPId(itemObject.getString("variation_id"));

            JSONObject jsonObject = itemObject.getJSONObject("variation_info");
            item.setPrice(jsonObject.getLong("retail_price_wholesale_original"));
            item.setCode(jsonObject.getString("product_id"));
            item.setName(jsonObject.getString("name"));
            items.add(item);
        }
        return items;
    }
}
