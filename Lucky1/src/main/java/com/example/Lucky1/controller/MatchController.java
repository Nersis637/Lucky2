package com.example.Lucky1.controller;

import com.example.Lucky1.model.Match;
import com.example.Lucky1.model.MatchStatus;
import com.example.Lucky1.service.MatchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public String showMatches(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/authorization";
        }
        model.addAttribute("matches", getFilteredMatches());
        return "listofmatches";
    }

    @GetMapping("/api/matches")
    @ResponseBody
    public List<Match> getFilteredMatchesJson() {
        return getFilteredMatches();
    }

    private List<Match> getFilteredMatches() {
        LocalDateTime now      = LocalDateTime.now();
        LocalDate    todayDate = LocalDate.now();
        LocalDateTime today03  = todayDate.atTime(3, 0);
        LocalDateTime cutoff   = now.isAfter(today03) ? today03 : today03.minusDays(1);

        return matchService.findAllMatches().stream()
                .filter(m -> {
                    String h = m.getTeamHome();
                    String a = m.getTeamAway();
                    return h != null && a != null
                            && !h.isBlank() && !a.isBlank()
                            && !"null".equalsIgnoreCase(h.trim())
                            && !"null".equalsIgnoreCase(a.trim());
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
                .sorted(Comparator.comparing(Match::getMatchDate))
                .toList();
    }
}