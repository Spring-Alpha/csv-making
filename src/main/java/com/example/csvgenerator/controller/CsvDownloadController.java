package com.example.csvgenerator.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.util.*;

@RestController
@RequestMapping("/api/csv")
public class CsvDownloadController {

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadCsv(@RequestBody String jsonInput) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse JSON to List of Maps
            List<Map<String, Object>> data = objectMapper.readValue(jsonInput, new TypeReference<>() {});
            if (data.isEmpty()) {
                return ResponseEntity.badRequest().body("No data found.".getBytes());
            }

            // Prepare CSV headers and writer
            Set<String> headers = data.get(0).keySet();
            StringWriter writer = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])));

            // Write data rows
            for (Map<String, Object> row : data) {
                List<String> values = headers.stream()
                        .map(key -> Objects.toString(row.getOrDefault(key, ""), ""))
                        .toList();
                csvPrinter.printRecord(values);
            }

            csvPrinter.flush();

            // Prepare CSV as downloadable file
            byte[] csvBytes = writer.toString().getBytes();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"data.csv\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Error: " + e.getMessage()).getBytes());
        }
    }
}
