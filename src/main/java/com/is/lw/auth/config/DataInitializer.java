package com.is.lw.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        String sql = """
            insert into _user("email", "password", "first_name", "last_name", "is_confirmed", "is_enabled", "role")
            values ('ADMIN@mail.ru', '$2a$10$ZJpQ/vibcVsyBdDNOpT71eF592o3Moy0CtHWcoLG55u.LIF6M5RaK', 'ADMIN', 'ADMIN', true, true, 'ADMIN');
        """;
        jdbcTemplate.execute(sql);
    }
}
