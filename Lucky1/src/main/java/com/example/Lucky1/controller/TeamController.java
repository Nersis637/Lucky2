package com.example.Lucky1.controller;

import com.example.Lucky1.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/fetch/{id}")
    public ResponseEntity<String> fetchAndSaveTeam(@PathVariable("id") int teamId) {
        try {
            teamService.fetchAndSaveTeam(teamId);
            return ResponseEntity.ok("Команда с ID " + teamId + " успешно сохранена в БД.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка: " + e.getMessage());
        }
    }
}
