package com.example.Lucky1.controller;

import com.example.Lucky1.dto.PlayerStatsRow;
import com.example.Lucky1.model.Match;
import com.example.Lucky1.model.Player;
import com.example.Lucky1.repository.MatchRepository;
import com.example.Lucky1.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @GetMapping("/player/{id}")
    public String getPlayerStats(@PathVariable("id") Integer id, Model model) {
        Optional<Player> playerOpt = playerRepository.findById(id);

        if (playerOpt.isEmpty()) {
            return "redirect:/search-page";
        }

        Player player = playerOpt.get();
        model.addAttribute("player", player);

        // Последние матчи клуба игрока
        String teamName = player.getTeam().getName();
        List<Match> lastMatches = matchRepository.findLastMatchesForTeam(teamName, PageRequest.of(0, 3));
        model.addAttribute("lastMatches", lastMatches);

        // Только статистика по клубу
        List<PlayerStatsRow> playerStatsList = new ArrayList<>();

        if (player.getTeam() != null) {
            PlayerStatsRow clubStats = new PlayerStatsRow();
            clubStats.setTeamName(player.getTeam().getName());
            clubStats.setCompetitionName("Клубный турнир");
            clubStats.setMatches(player.getMatches());
            clubStats.setGoals(player.getGoals());
            clubStats.setAssists(player.getAssists());
            clubStats.setMinutes(player.getMinutesPlayed());
            clubStats.setYellowCards(player.getYellowCards());
            clubStats.setRedCards(player.getRedCards());
            playerStatsList.add(clubStats);
        }

        model.addAttribute("playerStatsList", playerStatsList);

        return "player-stats";
    }
}
