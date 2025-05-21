package com.example.Lucky1.repository;

import com.example.Lucky1.model.Match;
import com.example.Lucky1.model.Tournament;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    /**
     * Находит матч по сочетанию домашней команды, гостевой команды и времени начала.
     */
    Optional<Match> findByTeamHomeAndTeamAwayAndMatchDate(
            String teamHome,
            String teamAway,
            LocalDateTime matchDate
    );

    /**
     * Находит все матчи, относящиеся к указанному турниру.
     */
    List<Match> findByTournament(Tournament tournament);

    /**
     * Возвращает последние матчи указанной команды (как домашняя или гостевая), отсортированные по дате.
     */
    @Query("SELECT m FROM Match m WHERE m.teamHome = :teamName OR m.teamAway = :teamName ORDER BY m.matchDate DESC")
    List<Match> findLastMatchesForTeam(@Param("teamName") String teamName, Pageable pageable);
}