package com.example.csvgenerator.service;

import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Service
public class CsvExportService {

    public String convertJsonToCsv(List<Map<String, Object>> jsonData) {
        StringWriter stringWriter = new StringWriter();

        System.out.println(jsonData);

        try (ICsvListWriter csvWriter = new CsvListWriter(stringWriter,
                CsvPreference.STANDARD_PREFERENCE)) {

            // Write header
            if (!jsonData.isEmpty()) {
                String[] header = jsonData.get(0).keySet().toArray(new String[0]);
                csvWriter.writeHeader(header);
            }

            // Write data
            for (Map<String, Object> row : jsonData) {
                csvWriter.write(row.values().stream()
                        .map(value -> value != null ? value.toString() : "")
                        .toArray());
            }

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CSV", e);
        }
    }
}
