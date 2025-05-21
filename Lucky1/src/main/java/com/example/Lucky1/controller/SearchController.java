package com.example.Lucky1.controller;

import com.example.Lucky1.model.Player;
import com.example.Lucky1.model.Team;
import com.example.Lucky1.repository.PlayerRepository;
import com.example.Lucky1.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/search")
    public String search(@RequestParam("query") String query,
                         RedirectAttributes redirectAttributes) {

        // 1. Попытка точного поиска по полному имени игрока
        List<Player> exactMatch = playerRepository.findByFullNameExact(query.trim());
        if (!exactMatch.isEmpty()) {
            Player player = exactMatch.get(0);
            System.out.println("Redirecting to player (full match): " + player.getPlayerId());
            return "redirect:/player/" + player.getPlayerId();
        }

        // 2. Поиск игрока по имени или фамилии (частичное совпадение)
        List<Player> partialPlayers = playerRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
        if (!partialPlayers.isEmpty()) {
            Player player = partialPlayers.get(0);
            System.out.println("Redirecting to player (partial match): " + player.getPlayerId());
            return "redirect:/player/" + player.getPlayerId();
        }

        // 3. Поиск команды по названию
        List<Team> teams = teamRepository.findByNameContainingIgnoreCase(query);
        if (!teams.isEmpty()) {
            Team team = teams.get(0);
            String encodedName = URLEncoder.encode(team.getName(), StandardCharsets.UTF_8);
            System.out.println("Redirecting to team: " + team.getName());
            return "redirect:/team/" + encodedName;
        }

        // 4. Если ничего не найдено
        redirectAttributes.addFlashAttribute("error", "Ничего не найдено по запросу: " + query);
        return "redirect:/search-page";
    }

    @GetMapping("/search-page")
    public String showSearchPage() {
        return "search";
    }
}