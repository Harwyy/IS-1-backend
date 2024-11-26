package com.is.lw.auth.repository;

import com.is.lw.auth.model.User;
import com.is.lw.auth.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByPassword(String password);

    List<User> findByRoleAndIsConfirmed(Role role, boolean isConfirmed);
}
