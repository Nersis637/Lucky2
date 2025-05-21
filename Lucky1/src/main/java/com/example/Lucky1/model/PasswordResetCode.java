package com.example.Lucky1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_codes")
public class PasswordResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String code;

    private LocalDateTime expiration;

    public PasswordResetCode() {
    }

    public PasswordResetCode(String email, String code, LocalDateTime expiration) {
        this.email = email;
        this.code = code;
        this.expiration = expiration;
    }

    // Getters and Setters

    public Long getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocalDateTime getExpiration() { return expiration; }
    public void setExpiration(LocalDateTime expiration) { this.expiration = expiration; }
}
