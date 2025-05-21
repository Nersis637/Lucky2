package com.example.Lucky1.controller;

import com.example.Lucky1.model.Player;
import com.example.Lucky1.model.Team;
import com.example.Lucky1.repository.PlayerRepository;
import com.example.Lucky1.repository.TeamRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/autocomplete")
public class AutocompleteSearchController {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public AutocompleteSearchController(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public Map<String, List<String>> autocomplete(@RequestParam String query) {
        List<String> players = playerRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
                .stream()
                .map(p -> p.getFirstName() + (p.getLastName() != null ? " " + p.getLastName() : ""))
                .collect(Collectors.toList());

        List<String> teams = teamRepository
                .findByNameContainingIgnoreCase(query)
                .stream()
                .map(Team::getName)
                .collect(Collectors.toList());

        Map<String, List<String>> result = new HashMap<>();
        result.put("players", players);
        result.put("teams", teams);
        return result;
    }
}
