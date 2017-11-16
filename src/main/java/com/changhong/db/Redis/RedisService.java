package com.changhong.db.Redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.changhong.util.MyStringUtils;
import org.bson.Document;

import java.util.*;

/**
 * Created by tangjuan on 2017/11/13.
 */
public class RedisService extends RedisHelper {

    public static Map<String, Document> getFilmFeatureMapInRedis(String field, String filmName) {
        Collection<String> coll = (Collection<String>) hget(field, filmName);
        Map<String, Document> map = new HashMap<>();
        if (coll != null && !coll.isEmpty()) {
            for (String str : coll) {
                Document doc = Document.parse(str);
                String _key = doc.getString("_key");
                if (_key != null) {
                    map.put(_key, doc);
                }
            }
        }
        return map;
    }

    public static void putMap2Redis(String field, Map<String, Map<String, String>> map) {
        if (map != null && !map.isEmpty()) {
            Map<String, String> tempMap = new HashMap<>();
            for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
                Map<String, String> tMap = entry.getValue();
                if (!tMap.isEmpty()) {
                    tempMap.put(entry.getKey(), tMap.values().toString());
                }
                if (tempMap.size() % 10000 == 0) {
                    putAll(field, tempMap);
                    tempMap.clear();
                }
            }
            putAll(field, tempMap);
        }
    }

    public static List getDupEntityInfoList(String formatFilmName, String label, Class clazz) {
        if (MyStringUtils.isNotBlank(formatFilmName) && MyStringUtils.isNotBlank(label)) {
            Object jsonArrayStr = hget(label, formatFilmName);
            if (jsonArrayStr != null) {
                return JSON.parseArray(jsonArrayStr.toString(), clazz);
            }
        }
        return null;
    }

}
