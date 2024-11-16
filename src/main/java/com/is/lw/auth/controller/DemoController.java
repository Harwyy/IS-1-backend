package com.is.lw.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DemoController {

    @GetMapping("/admin/hello")
    public ResponseEntity<String> demoAdmin() {
        return ResponseEntity.ok("Hello admin!");
    }

    @GetMapping("/user/hello")
    public ResponseEntity<String> demoUser() {
        return ResponseEntity.ok("Hello user!");
    }

}
