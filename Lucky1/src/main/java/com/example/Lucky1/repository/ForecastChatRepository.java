package com.example.Lucky1.repository;

import com.example.Lucky1.model.ForecastChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForecastChatRepository extends JpaRepository<ForecastChat, Long> {
    Optional<ForecastChat> findByTeam1IdAndTeam2Id(Long team1Id, Long team2Id);
}
