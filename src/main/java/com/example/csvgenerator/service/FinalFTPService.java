package com.example.csvgenerator.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FinalFTPService {

    @Value("${ftp.server.url}")
    private String ftpServer;

    @Value("${ftp.server.port}")
    private int ftpPort;

    @Value("${ftp.server.username}")
    private String ftpUsername;

    @Value("${ftp.server.password}")
    private String ftpPassword;

    public boolean uploadCsvFile(File file, String fileName) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpServer, ftpPort);
            boolean login = ftpClient.login(ftpUsername, ftpPassword);
            if (!login) {
                System.out.println("FTP login failed");
                return false;
            }

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.changeWorkingDirectory("/nettverk/csv/");

            try (InputStream input = new FileInputStream(file)) {
                boolean success = ftpClient.storeFile(fileName, input);
                ftpClient.logout();
                return success;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException ignored) {}
            if (file != null && file.exists()) {
                file.delete(); // cleanup temp file
            }
        }
    }
}
