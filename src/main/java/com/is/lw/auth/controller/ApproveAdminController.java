package com.is.lw.auth.controller;

import com.is.lw.auth.service.ApproveAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class ApproveAdminController {

    private final ApproveAdminService service;

    @PostMapping("/approve-admin")
    public ResponseEntity<ApproveAdminResponse> registerUser(@RequestBody ApproveAdminRequest request) {
        return ResponseEntity.ok(service.approveAdmin(request));
    }

}
