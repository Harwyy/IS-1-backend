package com.is.lw.auth.service;

import com.is.lw.auth.controller.request.ApproveAdminRequest;
import com.is.lw.auth.controller.response.ApproveAdminResponse;
import com.is.lw.auth.controller.response.WaitingForApproveResponse;
import com.is.lw.auth.model.User;
import com.is.lw.auth.model.enums.Role;
import com.is.lw.model.enums.Status;
import com.is.lw.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public ApproveAdminResponse approveAdmin(ApproveAdminRequest approveAdminRequest) {

        var user = userRepository.findByEmail(approveAdminRequest.getEmail()).orElseThrow();

        if (user.isConfirmed()){
            return ApproveAdminResponse.builder()
                    .status(Status.FAIL)
                    .message("User with mail "  + approveAdminRequest.getEmail () + " has already been confirmed.")
                    .build();
        }

        user.setConfirmed(true);
        user.setEnabled(true);
        userRepository.save(user);

        return ApproveAdminResponse.builder()
                .status(Status.SUCCESS)
                .build();
    }

    public WaitingForApproveResponse getWaitingForApprove() {
        List<User> users = userRepository.findByRoleAndIsConfirmed(Role.ADMIN, false);
        List<String> emails = new ArrayList<>();
        for (User user : users) {
            emails.add(user.getEmail());
        }
        return WaitingForApproveResponse.builder()
                .status(Status.SUCCESS)
                .list(emails)
                .length(emails.size())
                .build();
    }

}
