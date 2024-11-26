package com.is.lw.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final JdbcTemplate jdbcTemplate;

    public static String loadSqlFromFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() throws IOException {
        String sql = loadSqlFromFile("src/main/resources/sql/insertAdmin.sql");
//        String first = loadSqlFromFile("src/main/resources/sql/deleteMinPoint.sql");
        jdbcTemplate.execute(sql);
//        jdbcTemplate.execute(first);
    }
}
