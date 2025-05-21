package com.example.Lucky1.repository;

import com.example.Lucky1.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {

    // Лучшие бомбардиры
    @Query("SELECT p FROM Player p WHERE p.goals IS NOT NULL AND p.matches IS NOT NULL ORDER BY p.goals DESC")
    List<Player> findTopScorers();

    // Автодополнение по имени и фамилии
    List<Player> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    // Игроки команды по team_id
    List<Player> findByTeam_TeamId(int teamId);

    // Поиск по полному имени (например, "Diego Llorente")
    @Query("SELECT p FROM Player p WHERE " +
            "LOWER(CONCAT(p.firstName, ' ', p.lastName)) = LOWER(:fullName)")
    List<Player> findByFullNameExact(@Param("fullName") String fullName);
}
