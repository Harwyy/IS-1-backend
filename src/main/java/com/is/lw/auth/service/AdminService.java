package com.is.lw.auth.service;

import com.is.lw.auth.model.User;
import com.is.lw.auth.model.UserSummary;
import com.is.lw.auth.model.enums.Role;
import com.is.lw.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository repository;

    @Transactional(readOnly = true)
    public ResponseEntity<List<UserSummary>> getUnconfirmedAdmins() {
        List<User> unconfirmedAdmins = repository.findByRoleAndIsConfirmed(Role.ADMIN, false);
        if (unconfirmedAdmins.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        List<UserSummary> unconfirmedAdminSummaries = unconfirmedAdmins.stream()
                .map(user -> UserSummary.builder().id(user.getId()).username(user.getUsername()).build())
                .toList();

        return ResponseEntity.ok(unconfirmedAdminSummaries);
    }

    @Transactional
    public ResponseEntity<String> confirmAdmin(Long adminId) {
        User me = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!me.isConfirmed() || !me.isEnabled()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only a confirmed and enabled admin can confirm another admin.");
        }
        Optional<User> userToConfirm = repository.findById(adminId);
        if (userToConfirm.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found.");
        }
        User user = userToConfirm.get();
        if (user.isConfirmed()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("This admin is already confirmed.");
        }

        user.setConfirmed(true);
        user.setEnabled(true);
        repository.save(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Admin confirmed successfully.");
    }
}
