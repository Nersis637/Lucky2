package com.example.Lucky1.repository;

import com.example.Lucky1.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findBySessionToken(String sessionToken);
}