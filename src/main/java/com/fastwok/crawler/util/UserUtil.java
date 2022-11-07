package com.fastwok.crawler.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserUtil {
    public static String getListUser(JSONObject jsonValues){
        if (jsonValues.has("assignTo")) {
            if (jsonValues.getJSONArray("assignTo").length() > 0) {
                List<String> users = new ArrayList<>();
                for (int z = 0; z < jsonValues.getJSONArray("assignTo").length(); z++) {
                    JSONObject jsonObject = jsonValues.getJSONArray("assignTo").getJSONObject(z);
                    users.add(jsonObject.getString("name"));
                }
                if (users.size() > 0) {
                    return String.join(", ",users);
                }
            }
        }
        return null;
    }
}
