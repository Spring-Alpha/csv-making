package com.example.csvgenerator.controller;

import com.example.csvgenerator.service.FTPService;
import com.example.csvgenerator.util.JsonToCsvConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/csv")
public class CsvSaveController {

    private final FTPService ftpService;

    public CsvSaveController(FTPService ftpService) {
        this.ftpService = ftpService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveCsv(@RequestBody String jsonInput) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> data = mapper.readValue(jsonInput, new TypeReference<>() {
            });

            if (data.isEmpty()) {
                return ResponseEntity.badRequest().body("JSON data is empty.");
            }

            // Define file path
            String folderPath = "./exports";
            String filePath = folderPath + "/data" + Instant.now().toEpochMilli() + ".csv";

            // Ensure folder exists
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            try (var writer = new FileWriter(filePath);) {
                writer.write(JsonToCsvConverter.convertComplexJsonToCsv(jsonInput));
            }

            //ftpService.writeAndUploadCSV("demo" + Instant.now() + ".csv");
            ftpService.testUpload();

            return ResponseEntity.ok("CSV saved to: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}

