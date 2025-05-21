package com.example.csvgenerator.service;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class SftpService {

    @Value("${sftp.server.url}")
    private String sftpHost;

    @Value("${sftp.server.port}")
    private int sftpPort;

    @Value("${sftp.server.username}")
    private String sftpUsername;

    @Value("${sftp.server.password}")
    private String sftpPassword;

    public boolean uploadFile(File csvFile, String remoteFileName) {
        Session session = null;
        Channel channel = null;
        InputStream input = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sftpUsername, sftpHost, sftpPort);
            session.setPassword(sftpPassword);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;

            input = new FileInputStream(csvFile);
            sftp.put(input, remoteFileName);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (input != null) {
                try {
                    input.close(); // explicitly close stream
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (channel != null) channel.disconnect();
            if (session != null) session.disconnect();

            if (csvFile != null && csvFile.exists()) {
                boolean deleted = csvFile.delete();
                System.out.println("Temp file deleted: " + deleted + " - " + csvFile.getAbsolutePath());
            }
        }
    }
}
