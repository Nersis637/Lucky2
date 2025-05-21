package com.example.Lucky1.controller;

import com.example.Lucky1.model.Match;
import com.example.Lucky1.model.MatchStatus;
import com.example.Lucky1.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
public class MatchRestController {

    private final MatchService matchService;

    @Autowired
    public MatchRestController(MatchService matchService) {
        this.matchService = matchService;
    }

    /**
     * API /api/matches?sort=ASC|DESC&league=...&homeTeam=...&limit=7
     */
    @GetMapping("/api/matches")
    public List<Match> getFilteredMatches(
            @RequestParam(defaultValue = "ASC") String sort,
            @RequestParam(required = false) String league,
            @RequestParam(required = false) String homeTeam,
            @RequestParam(defaultValue = "40") int limit // 👈 добавлен параметр
    ) {
        LocalDateTime now = LocalDateTime.now();

        return matchService.findAllMatches().stream()
                .filter(m -> {
                    String h = m.getTeamHome();
                    String a = m.getTeamAway();
                    return h != null && a != null &&
                            !h.isBlank() && !a.isBlank() &&
                            !"null".equalsIgnoreCase(h.trim()) &&
                            !"null".equalsIgnoreCase(a.trim());
                })
                .filter(m -> {
                    LocalDateTime matchTime = m.getMatchDate();
                    long minutesSinceStart = Duration.between(matchTime, now).toMinutes();

                    if (m.getStatus() == MatchStatus.SCHEDULED) {
                        return matchTime.isAfter(now.minusHours(3));
                    }
                    if (m.getStatus() == MatchStatus.ONGOING) {
                        return minutesSinceStart >= 0 && minutesSinceStart <= 180;
                    }
                    return false;
                })
                .filter(m -> {
                    if (league == null || league.isBlank()) return true;
                    return m.getTournament() != null &&
                            m.getTournament().getName().equalsIgnoreCase(league.trim());
                })
                .filter(m -> {
                    if (homeTeam == null || homeTeam.isBlank()) return true;
                    String query = homeTeam.trim().toLowerCase(Locale.ROOT);
                    return m.getTeamHome().toLowerCase(Locale.ROOT).contains(query)
                            || m.getTeamAway().toLowerCase(Locale.ROOT).contains(query);
                })
                .sorted((m1, m2) -> {
                    if (sort.equalsIgnoreCase("DESC")) {
                        return m2.getMatchDate().compareTo(m1.getMatchDate());
                    } else {
                        return m1.getMatchDate().compareTo(m2.getMatchDate());
                    }
                })
                .limit(limit) // 👈 применяем лимит
                .collect(Collectors.toList());
    }
}
