package com.is.lw.auth.controller;

import com.is.lw.auth.controller.request.ApproveAdminRequest;
import com.is.lw.auth.controller.response.ApproveAdminResponse;
import com.is.lw.auth.controller.response.WaitingForApproveResponse;
import com.is.lw.auth.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @PostMapping("/approve-admin")
    public ResponseEntity<ApproveAdminResponse> registerUser(@RequestBody ApproveAdminRequest request) {
        return ResponseEntity.ok(adminService.approveAdmin(request));
    }
    @GetMapping("/list-of-admins-on-approve")
    public ResponseEntity<WaitingForApproveResponse> registerUser() {
        return ResponseEntity.ok(adminService.getWaitingForApprove());
    }


}
