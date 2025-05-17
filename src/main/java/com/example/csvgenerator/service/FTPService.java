package com.example.csvgenerator.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FTPService {

    @Value("${ftp.server.url}")
    private String ftpServer;

    @Value("${ftp.server.port}")
    private int ftpPort;

    @Value("${ftp.server.username}")
    private String ftpUsername;

    @Value("${ftp.server.password}")
    private String ftpPassword;

    public File createTempCsvFile() throws IOException {
        // Create temp file with a unique name
        File tempFile = File.createTempFile("demo-", ".csv");

        // Write sample CSV content
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("ID,Name,Email\n");
            writer.write("1,Alice,alice@example.com\n");
            writer.write("2,Bob,bob@example.com\n");
        }

        System.out.println("Temp file created at: " + tempFile.getAbsolutePath());
        return tempFile;
    }

    public boolean testUpload () throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(ftpServer, ftpPort);
        ftpClient.login(ftpUsername, ftpPassword);

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        ftpClient.changeWorkingDirectory("/My Documents/");

        boolean done = false;
        try (InputStream input = new FileInputStream(createTempCsvFile())) {
            done = ftpClient.storeFile("uploaded_test.csv", input);
            System.out.println("Upload success: " + done);
            System.out.println("Server reply: " + ftpClient.getReplyString());
        }
        ftpClient.logout();
        ftpClient.disconnect();
        return done;
    }

    public boolean writeAndUploadCSV(String remoteFileName) {
        FTPClient ftpClient = new FTPClient();
        File tempFile = null;

        try {
            // Step 1: Write CSV to a temporary file
            tempFile = File.createTempFile("data", ".csv");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write("ID,Name,Email\n");
                writer.write("1,John Doe,john@example.com\n");
                writer.write("2,Jane Smith,jane@example.com\n");
            }

            // Step 2: Connect to FTP and upload the file
            ftpClient.connect(ftpServer, ftpPort);
            boolean login = ftpClient.login(ftpUsername, ftpPassword);
            if (!login) {
                System.out.println("FTP login failed");
                return false;
            }

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            try (InputStream input = new FileInputStream(tempFile)) {
                boolean success = ftpClient.storeFile(remoteFileName, input);
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
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete(); // cleanup temp file
            }
        }
    }
}
