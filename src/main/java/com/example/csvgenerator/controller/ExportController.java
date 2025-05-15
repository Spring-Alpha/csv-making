package com.example.csvgenerator.controller;

import com.example.csvgenerator.service.CsvExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exports")
public class ExportController {

    private final CsvExportService csvExportService;

    public ExportController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
    }

    @PostMapping(value = "/csv", produces = "text/csv")
    public ResponseEntity<String> exportToCsv(@RequestBody List<Map<String, Object>> jsonData) {
        String csvContent = csvExportService.convertJsonToCsv(jsonData);
        System.out.println(csvContent);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvContent);
    }
}