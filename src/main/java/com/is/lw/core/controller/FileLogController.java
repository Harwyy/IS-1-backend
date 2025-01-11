package com.is.lw.core.controller;

import com.is.lw.core.model.FileLog;
import com.is.lw.core.repository.FileLogRepository;
import com.is.lw.core.service.FileLogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/file-log")
public class FileLogController {

    private final FileLogService fileLogService;
    private final FileLogRepository repository;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/history")
    public ResponseEntity<List<FileLog>> getHistory() {
        return ResponseEntity.ok(fileLogService.listOfFileLogs());
    }

    @PostMapping("/upload")
    public ResponseEntity<FileLog> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        FileLog fileLog = fileLogService.importLabWorks(fileContent);
        repository.save(fileLog);
        return ResponseEntity.ok(fileLog);
    }
}