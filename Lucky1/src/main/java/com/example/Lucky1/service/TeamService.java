package com.example.Lucky1.service;

import com.example.Lucky1.model.Team;
import com.example.Lucky1.repository.TeamRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private static final String API_KEY = "9b06ff557abc442cacf92755cd9ae07d";
    private static final int REQUEST_DELAY_MS = 3000_000; // 30 секунд между запросами

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Запускается каждый день в 00:00
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleTeamParsing() {
        System.out.println("⏰ Плановый запуск парсинга команд в 00:00");
        fetchAndSaveAllTeamsFromAllCompetitions();
    }

    public void fetchAndSaveAllTeamsFromAllCompetitions() {
        List<Integer> competitionIds = fetchAllowedCompetitionIds();
        int totalSaved = 0;

        for (Integer competitionId : competitionIds) {
            System.out.println("▶ Получаем команды из турнира ID: " + competitionId);
            List<Integer> teamIds = fetchTeamIdsFromCompetition(competitionId);

            for (Integer teamId : teamIds) {
                try {
                    if (!teamRepository.existsById(teamId)) {
                        fetchAndSaveTeam(teamId);
                        totalSaved++;
                        System.out.println("✔ Сохранено: " + totalSaved + " команд");
                    } else {
                        System.out.println("↪ Пропущено (уже в БД): " + teamId);
                    }

                    Thread.sleep(REQUEST_DELAY_MS);
                } catch (Exception e) {
                    System.err.println("⚠ Ошибка при обработке teamId=" + teamId + ": " + e.getMessage());
                }
            }
        }

        System.out.println("✅ Загрузка завершена. Всего сохранено: " + totalSaved + " команд.");
    }

    private List<Integer> fetchAllowedCompetitionIds() {
        try {
            URL url = new URL("https://api.football-data.org/v4/competitions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Auth-Token", API_KEY);
            connection.setRequestMethod("GET");

            InputStream response = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);
            JsonNode competitions = json.path("competitions");

            List<Integer> ids = new ArrayList<>();
            for (JsonNode comp : competitions) {
                String plan = comp.path("plan").asText();
                if (plan.equals("TIER_ONE") || plan.equals("TIER_TWO")) {
                    ids.add(comp.path("id").asInt());
                }
            }

            System.out.println("📋 Получено турниров: " + ids.size());
            return ids;

        } catch (Exception e) {
            System.err.println("❌ Не удалось получить список соревнований: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<Integer> fetchTeamIdsFromCompetition(int competitionId) {
        try {
            URL url = new URL("https://api.football-data.org/v4/competitions/" + competitionId + "/teams");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Auth-Token", API_KEY);
            connection.setRequestMethod("GET");

            InputStream response = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);
            JsonNode teams = json.path("teams");

            return teams.findValuesAsText("id").stream().map(Integer::parseInt).toList();

        } catch (Exception e) {
            System.err.println("❌ Не удалось получить команды турнира ID " + competitionId + ": " + e.getMessage());
            return List.of();
        }
    }

    public void fetchAndSaveTeam(int teamId) throws Exception {
        URL url = new URL("https://api.football-data.org/v4/teams/" + teamId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-Auth-Token", API_KEY);
        connection.setRequestMethod("GET");

        InputStream response = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);

        Team team = new Team();
        team.setTeamId(json.get("id").asInt());
        team.setName(json.get("name").asText());
        team.setShortName(json.get("shortName").asText());
        team.setTla(json.get("tla").asText());
        team.setCountry(json.path("area").get("name").asText());

        String city = json.hasNonNull("address") ? extractCity(json.get("address").asText()) : "N/A";
        team.setCity(city);

        team.setCoach(json.path("coach").hasNonNull("name") ? json.get("coach").get("name").asText() : "N/A");
        team.setLogoUrl(json.hasNonNull("crest") ? json.get("crest").asText() : "N/A");
        team.setFounded(json.hasNonNull("founded") ? json.get("founded").asInt() : 0);
        team.setVenue(json.hasNonNull("venue") ? json.get("venue").asText() : "N/A");

        teamRepository.save(team);
    }

    private String extractCity(String address) {
        String[] parts = address.split(",");
        return parts.length > 0 ? parts[parts.length - 1].trim() : "N/A";
    }
}