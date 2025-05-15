package com.example.csvgenerator.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.StringWriter;
import java.util.*;

public class JsonToCsvConverter {

    public static String convertJsonToCsv(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Parse JSON string to List of Maps
        List<Map<String, Object>> data = mapper.readValue(jsonString, new TypeReference<>() {});

        if (data.isEmpty()) return "";

        StringWriter out = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(data.get(0).keySet().toArray(new String[0])));

        for (Map<String, Object> row : data) {
            csvPrinter.printRecord(row.values());
        }

        csvPrinter.flush();
        return out.toString();
    }

    public static String convertJsonToCsvNested(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> rawList = mapper.readValue(jsonString, new TypeReference<>() {});
        List<Map<String, Object>> flatList = new ArrayList<>();

        Set<String> headers = new LinkedHashSet<>();

        for (Map<String, Object> item : rawList) {
            Map<String, Object> flat = JsonFlattener.flatten(item);
            flatList.add(flat);
            headers.addAll(flat.keySet());
        }

        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])));

        for (Map<String, Object> row : flatList) {
            List<String> record = headers.stream()
                    .map(key -> row.getOrDefault(key, "").toString())
                    .toList();
            printer.printRecord(record);
        }

        printer.flush();
        return out.toString();
    }

    public static String convertComplexJsonToCsv(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Object> records = mapper.readValue(jsonString, new TypeReference<>() {});
        List<Map<String, String>> flatList = new ArrayList<>();
        Set<String> allKeys = new LinkedHashSet<>();

        for (Object record : records) {
            Map<String, String> flatMap = ComplexJsonFlattener.flatten(record);
            flatList.add(flatMap);
            allKeys.addAll(flatMap.keySet());
        }

        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(allKeys.toArray(new String[0])));

        for (Map<String, String> record : flatList) {
            List<String> row = allKeys.stream().map(key -> record.getOrDefault(key, "")).toList();
            printer.printRecord(row);
        }

        printer.flush();
        return out.toString();
    }
}
