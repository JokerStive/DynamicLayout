package com.lilun.passionlife.cloudplatform.utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/12.
 */
public class JsonUtils {

    public static Map<String,Map<String,String>> jsonToMap(String json) throws JSONException {
        Map<String,Map<String,String>> map = new HashMap<>();

        JSONObject jsonObject = new JSONObject(json);
        for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();) {
            String key = (String) iter.next();
            String value = jsonObject.get(key).toString();
            JSONObject jsonObject1 = new JSONObject(json);
            for (Iterator<?> iter1 = jsonObject.keys(); iter.hasNext();) {
                String key1 = (String) iter1.next();
                String value1 = jsonObject.get(key).toString();
                Map<String,String> map1 = new HashMap<>();
                map1.put(key1,value1);
                map.put(key,map1);
            }
        }
        return map;
    }
}
