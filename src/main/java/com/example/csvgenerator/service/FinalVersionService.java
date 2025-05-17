package com.example.csvgenerator.service;

import com.example.csvgenerator.util.ComplexJsonFlattener;
import com.example.csvgenerator.util.JsonToCsvConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.*;

@Service
public class FinalVersionService {

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

    public File createCsvFile(String inputJson) {
        try {
            String csvString = convertComplexJsonToCsv(inputJson);
            File tempFile = File.createTempFile("demo-", ".csv");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(csvString);
            }

            System.out.println("Temp file created at: " + tempFile.getAbsolutePath());
            return tempFile;
        } catch (Exception e) {
            throw new RuntimeException("File creation failed");
        }
    }
}
