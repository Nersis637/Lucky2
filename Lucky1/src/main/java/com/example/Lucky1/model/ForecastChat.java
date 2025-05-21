package com.example.Lucky1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "forecast_chat", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"team1_id", "team2_id"})
})
public class ForecastChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @Column(name = "team1_id", nullable = false)
    private Long team1Id;

    @Column(name = "team2_id", nullable = false)
    private Long team2Id;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Геттеры и сеттеры ---
    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(Long team1Id) {
        this.team1Id = team1Id;
    }

    public Long getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(Long team2Id) {
        this.team2Id = team2Id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}