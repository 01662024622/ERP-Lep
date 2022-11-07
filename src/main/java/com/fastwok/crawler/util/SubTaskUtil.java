package com.fastwok.crawler.util;

import lombok.extern.java.Log;
import org.json.JSONArray;
import org.json.JSONObject;

public class SubTaskUtil {

    public static String getOverSubTask(JSONObject jsonValues) {
        if (jsonValues.has("checklists")) {
            if (jsonValues.getJSONArray("checklists").length() ==0) return null;
            if (jsonValues.getJSONArray("checklists").length() > 1) {
                if (jsonValues.getJSONArray("checklists").getJSONObject(1).has("checkItems")) {
                    if (jsonValues.getJSONArray("checklists").getJSONObject(1).getJSONArray("checkItems").length() > 0) {
                        JSONArray jsonArr = jsonValues.getJSONArray("checklists").getJSONObject(1).getJSONArray("checkItems");
                        for (int x = 0; x < jsonArr.length(); x++) {
                            if (jsonArr.getJSONObject(x).getInt("completed") == 0) {
                                return "Bước " + (x + 1) + ": " + jsonArr.getJSONObject(x).getString("title");
                            }
                        }
                    }
                }
            } else {
                if (jsonValues.getJSONArray("checklists").getJSONObject(0).has("checkItems")) {
                    if (jsonValues.getJSONArray("checklists").getJSONObject(0).getJSONArray("checkItems").length() > 0) {
                        JSONArray jsonArr = jsonValues.getJSONArray("checklists").getJSONObject(0).getJSONArray("checkItems");
                        for (int x = 0; x < jsonArr.length(); x++) {
                            if (jsonArr.getJSONObject(x).getInt("completed") == 0) {
                                return "Bước " + (x + 1) + ": " + jsonArr.getJSONObject(x).getString("title");
                            }
                        }
                    }
                }
            }

        }
        return null;
    }
}
