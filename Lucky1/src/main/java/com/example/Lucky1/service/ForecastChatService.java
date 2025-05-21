package com.example.Lucky1.service;

import com.example.Lucky1.model.ForecastChat;
import com.example.Lucky1.repository.ForecastChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForecastChatService {

    @Autowired
    private ForecastChatRepository chatRepository;

    public ForecastChat getOrCreateChat(Long team1Id, Long team2Id) {
        Optional<ForecastChat> existingChat = chatRepository.findByTeam1IdAndTeam2Id(team1Id, team2Id);
        return existingChat.orElseGet(() -> {
            ForecastChat newChat = new ForecastChat();
            newChat.setTeam1Id(team1Id);
            newChat.setTeam2Id(team2Id);
            return chatRepository.save(newChat);
        });
    }
}
