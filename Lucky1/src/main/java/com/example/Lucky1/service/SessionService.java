package com.example.Lucky1.service;

import com.example.Lucky1.model.Session;
import com.example.Lucky1.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public void saveSession(Session session) {
        sessionRepository.save(session);
    }

    public Session getSessionByToken(String token) {
        return sessionRepository.findBySessionToken(token);
    }
}