package com.example.csvgenerator.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonFlattener {
    public static Map<String, Object> flatten(Map<String, Object> map) {
        return flatten("", map);
    }

    private static Map<String, Object> flatten(String prefix, Map<String, Object> map) {
        Map<String, Object> flatMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                flatMap.putAll(flatten(key, (Map<String, Object>) value));
            } else {
                flatMap.put(key, value);
            }
        }
        return flatMap;
    }
}
