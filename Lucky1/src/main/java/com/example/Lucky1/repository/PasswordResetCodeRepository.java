package com.example.Lucky1.repository;

import com.example.Lucky1.model.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByEmail(String email);
    Optional<PasswordResetCode> findByEmailAndCode(String email, String code);
}
