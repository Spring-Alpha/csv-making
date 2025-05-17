package com.example.csvgenerator.controller;

import com.example.csvgenerator.service.FinalFTPService;
import com.example.csvgenerator.service.FinalVersionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.Instant;

@RequestMapping("/api/final/csv")
@RestController
public class FinalVersionController {

    private final FinalVersionService service;
    private final FinalFTPService ftpService;

    public FinalVersionController(FinalVersionService service,
                                  FinalFTPService ftpService) {
        this.service = service;
        this.ftpService = ftpService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveCsvFile(@RequestBody String jsonInput) {
        File finalFile = service.createCsvFile(jsonInput);
        String fileName = "csv_" + Instant.now().toEpochMilli() + ".csv";
        boolean status = ftpService.uploadCsvFile(finalFile, fileName);

        if(status){
            return ResponseEntity.ok("Successfully uploaded the file to DriveHQ server!");
        }
        return ResponseEntity.ok("Upload wasn't successful!");
    }
}
