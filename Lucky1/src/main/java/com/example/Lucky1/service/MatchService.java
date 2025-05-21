package com.example.Lucky1.service;

import com.example.Lucky1.model.Match;
import com.example.Lucky1.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match getMatchById(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with id: " + matchId));
    }

    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }

    public List<Match> findMatchesForTodaySorted() {
        LocalDate today = LocalDate.now();
        return matchRepository.findAll().stream()
                .filter(m -> m.getTeamHome() != null && m.getTeamAway() != null)
                .filter(m -> m.getMatchDate().toLocalDate().equals(today))
                .sorted((a, b) -> a.getMatchDate().compareTo(b.getMatchDate()))
                .toList();
    }

    public List<Match> findTop7MatchesForToday() {
        return findMatchesForTodaySorted().stream()
                .limit(7)
                .toList();
    }
}
