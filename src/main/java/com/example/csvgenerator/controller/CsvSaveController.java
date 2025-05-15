package com.example.csvgenerator.controller;

import com.example.csvgenerator.util.JsonToCsvConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/csv")
public class CsvSaveController {

    @PostMapping("/save")
    public ResponseEntity<String> saveCsv(@RequestBody String jsonInput) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> data = mapper.readValue(jsonInput, new TypeReference<>() {});

            if (data.isEmpty()) {
                return ResponseEntity.badRequest().body("JSON data is empty.");
            }

            // Define file path
            String folderPath = "./exports";
            String filePath = folderPath + "/data" + Instant.now().toEpochMilli()+".csv";

            // Ensure folder exists
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            try(var writer = new FileWriter(filePath);) {
                writer.write(JsonToCsvConverter.convertComplexJsonToCsv(jsonInput));
            }


            return ResponseEntity.ok("CSV saved to: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}

