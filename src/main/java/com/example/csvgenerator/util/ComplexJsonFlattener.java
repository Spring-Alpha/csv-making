package com.example.csvgenerator.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ComplexJsonFlattener {

    public static Map<String, String> flatten(Object current) {
        return flatten("", current, new LinkedHashMap<>());
    }

    private static Map<String, String> flatten(String path, Object current, Map<String, String> result) {
        if (current instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String newPath = path.isEmpty() ? entry.getKey().toString() : path + "." + entry.getKey();
                flatten(newPath, entry.getValue(), result);
            }
        } else if (current instanceof List<?> list) {
            for (int i = 0; i < list.size(); i++) {
                String newPath = path + "[" + i + "]";
                flatten(newPath, list.get(i), result);
            }
        } else {
            result.put(path, current != null ? current.toString() : "");
        }
        return result;
    }
}
