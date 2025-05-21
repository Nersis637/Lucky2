package com.example.Lucky1.controller;

import com.example.Lucky1.model.Team;
import com.example.Lucky1.repository.TeamRepository;
import com.example.Lucky1.repository.PlayerRepository;
import com.example.Lucky1.model.Player;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
public class TeamStatsController {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamStatsController(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/team/{name}")
    public String showTeamStats(@PathVariable("name") String encodedName, Model model) {
        String name = URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
        Optional<Team> teamOpt = teamRepository.findByName(name);

        if (teamOpt.isEmpty()) {
            return "redirect:/search-page"; // команда не найдена
        }

        Team team = teamOpt.get();
        model.addAttribute("team", team);

        // Добавляем игроков команды
        List<Player> players = playerRepository.findByTeam_TeamId(team.getTeamId());
        model.addAttribute("players", players);

        return "team-stats"; // шаблон team-stats.html
    }
}