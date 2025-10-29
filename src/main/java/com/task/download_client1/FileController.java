package com.task.download_client1;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.file.*;

@RestController
public class FileController {

    //This is to setup the path and name of the file/type
    private final Path filePath = Path.of("HOME", "file_to_download.txt").toAbsolutePath();

    //simply runs when starting the project, make a file
    @PostConstruct
    public void createSampleFile() throws IOException {
        Files.createDirectories(filePath.getParent());
        if (!Files.exists(filePath) || Files.size(filePath) < 100_000_000L) {
            try (OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                byte[] buffer = new byte[1024 * 1024]; // 1MB buffer
                for (int i = 0; i < 100; i++) os.write(buffer);
            }
        }
    }

    //for API aka postman to call and save in folder, root of this repo
    @GetMapping("/file/download")
    public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
        InputStreamResource resource = new InputStreamResource(Files.newInputStream(filePath));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file_to_download.txt\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(Files.size(filePath))
                .body(resource);
    }
}