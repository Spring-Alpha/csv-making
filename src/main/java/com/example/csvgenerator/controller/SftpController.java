package com.example.csvgenerator.controller;

import com.example.csvgenerator.service.FinalVersionService;
import com.example.csvgenerator.service.SftpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.Instant;

@RequestMapping("/api/sftp/csv")
@RestController
public class SftpController {
    private final SftpService service;
    private final FinalVersionService fileService;

    public SftpController(SftpService service, FinalVersionService fileService) {
        this.service = service;
        this.fileService = fileService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveCsvFile(@RequestBody String jsonInput) {
        File finalFile = fileService.createCsvFile(jsonInput);
        String fileName = "csv_" + Instant.now().toEpochMilli() + ".csv";
        boolean status = service.uploadFile(finalFile, fileName);

        if(status){
            return ResponseEntity.ok("Successfully uploaded the file to DriveHQ server!");
        }
        return ResponseEntity.ok("Upload wasn't successful!");
    }
}
