package com.template.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * packageName    : com.template.util
 * fileName       : JsonUtil
 * author         : Jihun Park
 * date           : 2022/03/18
 * description    : JsonUtil
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/18        Jihun Park       최초 생성
 */
public class JsonUtil {

    /**
     * methodName : convertToUserMap
     * author : Jihun Park
     * description : convertToUserMap (flat)
     * @param users
     * @return map
     */
    public static Map<String, Object> convertToUserMap(List<JSONObject> users)  {

        Map<String,Object> userMap = new LinkedHashMap<>();
        int idx = 0;
        for (JSONObject user : users) {
            Iterator<String> keys = user.keySet().iterator();

            while(keys.hasNext()) {
                String key = keys.next();
                Object value = user.get(key);
                if (value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                } else if (value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                userMap.put(key, value);
            }
        }
        return userMap;
    }

    public static Map<String, Object> toMap(JSONObject jsonobj)  {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keys = jsonobj.keySet().iterator();

        while(keys.hasNext()) {
            String key = keys.next();
            Object value = jsonobj.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Map<String, Object>> toMap(List<JSONObject> jsonobjs)  {
        List<Map<String, Object>> result = new ArrayList<>();
        for (JSONObject jsonobj : jsonobjs) {
            Map<String, Object> map = new HashMap<String, Object>();
            Iterator<String> keys = jsonobj.keySet().iterator();

            while(keys.hasNext()) {
                String key = keys.next();
                Object value = jsonobj.get(key);
                if (value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                } else if (value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                map.put(key, value);
            }   result.add(map);
        }
        return result;
    }

    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }   return list;
    }
}
