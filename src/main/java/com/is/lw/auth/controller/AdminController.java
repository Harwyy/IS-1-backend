package com.is.lw.auth.controller;

import com.is.lw.auth.controller.request.ApproveAdminRequest;
import com.is.lw.auth.controller.response.ApproveAdminResponse;
import com.is.lw.auth.controller.response.WaitingForApproveResponse;
import com.is.lw.auth.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin Controller", description = "Endpoints for admin-specific operations")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Confirmation of the admin account", description = "The administrator can confirm the account of a new administrator who is on the confirmation waiting list.")
    @PostMapping("/approve-admin")
    public ResponseEntity<ApproveAdminResponse> approveAdmin(@RequestBody ApproveAdminRequest request) {
        return ResponseEntity.ok(adminService.approveAdmin(request));
    }

    @Operation(summary = "List The list of admins waiting for account confirmation")
    @GetMapping("/list-of-admins-on-approve")
    public ResponseEntity<WaitingForApproveResponse> getWaitingList() {
        return ResponseEntity.ok(adminService.getWaitingForApprove());
    }

}
