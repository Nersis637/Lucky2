package com.example.Lucky1.repository;

import com.example.Lucky1.model.ForecastComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForecastCommentRepository extends JpaRepository<ForecastComment, Long> {
    List<ForecastComment> findByChatIdOrderByCreatedAtAsc(Long chatId);
}
