package com.example.Lucky1.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FootballDataTeamDetails {
    private static final String API_KEY = "9b06ff557abc442cacf92755cd9ae07d";
    private static final int TEAM_ID = 66; // Manchester United

    public static void main(String[] args) throws Exception {
        URL url = new URL("https://api.football-data.org/v4/teams/" + TEAM_ID);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-Auth-Token", API_KEY);
        connection.setRequestMethod("GET");

        InputStream response = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);

        // Основная информация о команде
        String name = json.get("name").asText();
        String shortName = json.get("shortName").asText();
        String tla = json.get("tla").asText();
        String country = json.get("area").get("name").asText();
        int founded = json.hasNonNull("founded") ? json.get("founded").asInt() : 0;
        String venue = json.hasNonNull("venue") ? json.get("venue").asText() : "N/A";
        String logo = json.hasNonNull("crest") ? json.get("crest").asText() : "N/A";

        // Город (извлекаем из адреса)
        String address = json.hasNonNull("address") ? json.get("address").asText() : "N/A";
        String city = "N/A";
        if (!address.equals("N/A")) {
            String[] tokens = address.split("\\s+");
            for (int i = tokens.length - 1; i >= 0; i--) {
                String token = tokens[i].replaceAll("[^a-zA-Z]", ""); // убрать знаки и цифры
                if (!token.isEmpty() && token.length() > 2) {
                    city = token;
                    break;
                }
            }
        }

        JsonNode coach = json.path("coach");
        String coachName = coach.hasNonNull("name") ? coach.get("name").asText() : "N/A";

        System.out.println("Команда: " + name);
        System.out.println("Короткое имя: " + shortName);
        System.out.println("Аббревиатура: " + tla);
        System.out.println("Страна: " + country);
        System.out.println("Город: " + city);
        System.out.println("Основана в: " + founded);
        System.out.println("Стадион: " + venue);
        System.out.println("Главный тренер: " + coachName);
        System.out.println("Логотип: " + logo);

        // Соревнования
        System.out.println("\nСоревнования:");
        JsonNode competitions = json.path("runningCompetitions");
        if (competitions.isArray()) {
            for (JsonNode comp : competitions) {
                String compName = comp.get("name").asText();
                String compCode = comp.get("code").asText();
                System.out.println(" - " + compName + " (" + compCode + ")");
            }
        }

        // Состав игроков
        System.out.println("\nСостав команды:");
        JsonNode squad = json.path("squad");
        if (squad.isArray()) {
            for (JsonNode player : squad) {
                String playerName = player.hasNonNull("name") ? player.get("name").asText() : "N/A";
                String position = player.hasNonNull("position") ? player.get("position").asText() : "N/A";
                System.out.println(" - " + playerName + " (" + position + ")");
            }
        }

        System.out.println("\nProcess finished with exit code 0");
    }
}