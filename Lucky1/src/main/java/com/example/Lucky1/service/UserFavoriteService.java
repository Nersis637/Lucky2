package com.example.Lucky1.service;

import com.example.Lucky1.model.Match;
import com.example.Lucky1.model.User;
import com.example.Lucky1.model.UserFavorite;
import com.example.Lucky1.repository.UserFavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFavoriteService {
    private final UserFavoriteRepository userFavoriteRepository;

    @Autowired
    public UserFavoriteService(UserFavoriteRepository userFavoriteRepository) {
        this.userFavoriteRepository = userFavoriteRepository;
    }

    @Transactional
    public void addFavorite(User user, Match match) {
        // проверку на дубликат можно оставить в контроллере или здесь
        if (!userFavoriteRepository.existsByUserAndMatch(user, match)) {
            UserFavorite userFavorite = new UserFavorite();
            userFavorite.setUser(user);
            userFavorite.setMatch(match);
            userFavoriteRepository.save(userFavorite);
        }
    }

    @Transactional
    public void removeFavorite(User user, Match match) {
        userFavoriteRepository.deleteByUserAndMatch(user, match);
    }

    public List<Match> getFavoritesForUser(User user) {
        return userFavoriteRepository.findByUser(user)
                .stream()
                .map(UserFavorite::getMatch)
                .collect(Collectors.toList());
    }

    public boolean isFavorite(User user, Match match) {
        return userFavoriteRepository.existsByUserAndMatch(user, match);
    }
}