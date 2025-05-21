package com.example.Lucky1.parser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FootyStatsParser {

    private static final String API_KEY = "test85g57";
    private static final String LEAGUE_ID = "1625"; // EPL 2018/2019
    private static final String URL_STR = "https://api.football-data-api.com/league-tables?key=" + API_KEY + "&league_id=" + LEAGUE_ID;

    public static void main(String[] args) throws Exception {
        URL url = new URL(URL_STR);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.toString());
        JsonNode table = root.get("data").get("all_matches_table_overall");

        if (table != null && table.isArray()) {
            for (JsonNode team : table) {
                int position = safeInt(team, "position");
                String name = team.has("name") ? team.get("name").asText() : "N/A";
                int matches = safeInt(team, "matchesPlayed");
                int wins = safeInt(team, "seasonWins_home") + safeInt(team, "seasonWins_away");
                int draws = safeInt(team, "seasonDraws_home") + safeInt(team, "seasonDraws_away");
                int losses = safeInt(team, "seasonLosses_home") + safeInt(team, "seasonLosses_away");
                int goals = safeInt(team, "seasonGoals");
                int conceded = safeInt(team, "seasonConceded_home") + safeInt(team, "seasonConceded_away");
                int points = safeInt(team, "points");

                System.out.println(position + ". " + name);
                System.out.println("Матчи: " + matches + ", Победы: " + wins + ", Ничьи: " + draws + ", Поражения: " + losses);
                System.out.println("Голы: " + goals + ", Пропущено: " + conceded + ", Очки: " + points);
                System.out.println("--------------------------------------------------");
            }
        } else {
            System.out.println("Не удалось получить таблицу лиги.");
        }
    }

    private static int safeInt(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull()
                ? node.get(fieldName).asInt()
                : 0;
    }
}

