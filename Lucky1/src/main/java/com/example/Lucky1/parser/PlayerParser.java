package com.example.Lucky1.parser;

import com.example.Lucky1.model.Player;
import com.example.Lucky1.model.Team;
import com.example.Lucky1.repository.PlayerRepository;
import com.example.Lucky1.repository.TeamRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class PlayerParser {

    private static final String API_KEY = "9b06ff557abc442cacf92755cd9ae07d";
    private static final int REQUEST_DELAY_MS = 9000; // 7 запросов в минуту

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public PlayerParser(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Плановый запуск каждый день в 5 утра
     */
    @Scheduled(cron = "0 0 5 * * *")
    public void scheduledPlayerParsing() {
        System.out.println("⏰ Плановый запуск парсера игроков в 05:00");
        parseAllPlayers();
    }

    public void parseAllPlayers() {
        List<Team> teams = teamRepository.findAll();

        for (Team team : teams) {
            try {
                parsePlayersByTeam(team);
                Thread.sleep(REQUEST_DELAY_MS);
            } catch (Exception e) {
                System.err.println("⚠ Ошибка при обработке команды " + team.getName() + ": " + e.getMessage());
            }
        }
    }

    private void parsePlayersByTeam(Team team) {
        try {
            URL url = new URL("https://api.football-data.org/v4/teams/" + team.getTeamId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Auth-Token", API_KEY);
            connection.setRequestMethod("GET");

            InputStream response = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);
            JsonNode squad = json.path("squad");

            if (squad.isArray()) {
                for (JsonNode playerJson : squad) {
                    int playerId = playerJson.get("id").asInt();
                    String firstName = playerJson.hasNonNull("firstName") ? playerJson.get("firstName").asText() : null;
                    String lastName = playerJson.hasNonNull("lastName") ? playerJson.get("lastName").asText() : null;

                    // fallback to name split
                    if (firstName == null && lastName == null && playerJson.hasNonNull("name")) {
                        String[] parts = playerJson.get("name").asText().split(" ", 2);
                        firstName = parts.length > 0 ? parts[0] : null;
                        lastName = parts.length > 1 ? parts[1] : null;
                    }

                    LocalDate dob = playerJson.hasNonNull("dateOfBirth") ?
                            LocalDate.parse(playerJson.get("dateOfBirth").asText()) : null;
                    Integer age = dob != null ? Period.between(dob, LocalDate.now()).getYears() : null;

                    String nationality = playerJson.hasNonNull("nationality") ?
                            playerJson.get("nationality").asText() : null;
                    String position = playerJson.hasNonNull("position") ?
                            playerJson.get("position").asText() : null;

                    // Парсим lastUpdated как OffsetDateTime и конвертируем в LocalDateTime
                    LocalDateTime lastUpdated = playerJson.hasNonNull("lastUpdated")
                            ? LocalDateTime.parse(playerJson.get("lastUpdated").asText().replace("Z", ""))
                            : null;

                    Player player = playerRepository.findById(playerId).orElse(null);
                    if (player == null) {
                        player = new Player();
                        player.setPlayerId(playerId);
                    }

                    boolean changed = false;
                    if (!equals(player.getFirstName(), firstName)) {
                        player.setFirstName(firstName);
                        changed = true;
                    }
                    if (!equals(player.getLastName(), lastName)) {
                        player.setLastName(lastName);
                        changed = true;
                    }
                    if (!equals(player.getDateOfBirth(), dob)) {
                        player.setDateOfBirth(dob);
                        changed = true;
                    }
                    if (!equals(player.getAge(), age)) {
                        player.setAge(age);
                        changed = true;
                    }
                    if (!equals(player.getNationality(), nationality)) {
                        player.setNationality(nationality);
                        changed = true;
                    }
                    if (!equals(player.getPosition(), position)) {
                        player.setPosition(position);
                        changed = true;
                    }
                    if (!equals(player.getLastUpdated(), lastUpdated)) {
                        player.setLastUpdated(lastUpdated);
                        changed = true;
                    }
                    if (!equals(player.getTeam(), team)) {
                        player.setTeam(team);
                        changed = true;
                    }

                    if (changed) {
                        playerRepository.save(player);
                        System.out.println("✔ Игрок обновлён: " + playerId);
                    } else {
                        System.out.println("↪ Игрок не изменился: " + playerId);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Ошибка при парсинге игроков команды " + team.getTeamId() + ": " + e.getMessage());
        }
    }

    private boolean equals(Object a, Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}