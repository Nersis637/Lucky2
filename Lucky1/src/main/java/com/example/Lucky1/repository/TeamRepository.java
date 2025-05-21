package com.example.Lucky1.repository;

import com.example.Lucky1.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    // Проверка, существует ли команда с указанным teamId
    boolean existsByTeamId(int teamId);

    // Команды с очками ЛЧ
    @Query("SELECT t FROM Team t WHERE t.championsLeaguePoints IS NOT NULL ORDER BY t.championsLeaguePoints DESC")
    List<Team> findChampionsLeagueRanking();

    // Автодополнение по имени команды
    List<Team> findByNameContainingIgnoreCase(String name);

    // Новый метод — найти точное совпадение по имени
    Optional<Team> findByName(String name);
}