package com.example.csvgenerator.controller;

import com.example.csvgenerator.util.JsonToCsvConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/convert")
public class JsonToCsvController {

    @PostMapping("/json-to-csv")
    public ResponseEntity<String> convert(@RequestBody String jsonInput) {
        try {
            String csvOutput = JsonToCsvConverter.convertJsonToCsv(jsonInput);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(csvOutput);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON: " + e.getMessage());
        }
    }

    @PostMapping("/json-to-csv/nested")
    public ResponseEntity<String> convertNested(@RequestBody String jsonInput) {
        try {
            String csvOutput = JsonToCsvConverter.convertJsonToCsvNested(jsonInput);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(csvOutput);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON: " + e.getMessage());
        }
    }

    @PostMapping("/json-to-csv/complex")
    public ResponseEntity<String> convertComplex(@RequestBody String jsonInput) {
        try {
            String csvOutput = JsonToCsvConverter.convertComplexJsonToCsv(jsonInput);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(csvOutput);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON: " + e.getMessage());
        }
    }
}

