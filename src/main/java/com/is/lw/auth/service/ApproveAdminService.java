package com.is.lw.auth.service;

import com.is.lw.auth.controller.ApproveAdminRequest;
import com.is.lw.auth.controller.ApproveAdminResponse;
import com.is.lw.auth.model.enums.Status;
import com.is.lw.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApproveAdminService {

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

}
