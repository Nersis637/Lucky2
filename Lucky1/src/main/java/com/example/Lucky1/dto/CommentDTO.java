package com.example.Lucky1.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private String username;
    private String message;
    private LocalDateTime createdAt;

    public CommentDTO(String username, String message, LocalDateTime createdAt) {
        this.username = username;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
