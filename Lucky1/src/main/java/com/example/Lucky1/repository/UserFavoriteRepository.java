package com.example.Lucky1.repository;

import com.example.Lucky1.model.Match;
import com.example.Lucky1.model.User;
import com.example.Lucky1.model.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    List<UserFavorite> findByUser(User user);
    void deleteByUserAndMatch(User user, Match match);
    boolean existsByUserAndMatch(User user, Match match);
}