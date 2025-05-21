package com.example.Lucky1.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/previous-matches")
public class PreviousMatchController {

    @GetMapping
    public List<Map<String, String>> getPreviousMatches(
            @RequestParam String team1,
            @RequestParam String team2
    ) throws IOException {
        List<Map<String, String>> result = new ArrayList<>();
        Set<String> seenMatches = new HashSet<>();

        // Сопоставление: название команды (в CSV) → имя файла картинки
        Map<String, String> logoMap = new HashMap<>();
        logoMap.put("Ростов", "rostov.png");
        logoMap.put("Спартак", "spartak.png");
        logoMap.put("Арсенал", "arsenal.png");
        logoMap.put("Амкар", "amkar.png");
        logoMap.put("ЦСКА", "cska.png");
        logoMap.put("Локомотив", "lokomotiv.png");
        logoMap.put("Рубин", "rubin.png");
        logoMap.put("Краснодар", "krasnodar.png");
        logoMap.put("Динамо", "dinamo.png");
        logoMap.put("Мордовия", "mordovia.png");
        logoMap.put("Кубань", "kuban.png");
        logoMap.put("Уфа", "ufa.png");
        logoMap.put("Анжи", "anji.png");
        logoMap.put("Ахмат", "ahmat.png");
        logoMap.put("Оренбург", "orenburg.png");
        logoMap.put("Ювентус", "juventus_logo.png");
        logoMap.put("Зенит", "zenit.png");
        // добавь сюда остальные, если нужно

        ClassPathResource csvFile = new ClassPathResource("static/data/RPL.csv");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine(); // skip header
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 8) continue;

                String home = parts[2].trim();
                String away = parts[3].trim();
                String goalsFor = parts[6].trim();
                String goalsAgainst = parts[7].trim();
                String year = parts[1].trim();

                boolean isMatchBetween =
                        (home.equalsIgnoreCase(team1) && away.equalsIgnoreCase(team2)) ||
                                (home.equalsIgnoreCase(team2) && away.equalsIgnoreCase(team1));

                if (!isMatchBetween) continue;

                // Упорядочим команды по алфавиту
                String teamA = home.compareToIgnoreCase(away) < 0 ? home : away;
                String teamB = home.compareToIgnoreCase(away) < 0 ? away : home;

                // Упорядочим счёт соответственно
                String score1 = home.equals(teamA) ? goalsFor : goalsAgainst;
                String score2 = home.equals(teamA) ? goalsAgainst : goalsFor;

                String matchKey = year + "_" + teamA + "_" + teamB + "_" + score1 + "_" + score2;

                if (seenMatches.contains(matchKey)) continue;
                seenMatches.add(matchKey);

                // Получаем логотипы или fallback
                String homeLogo = logoMap.getOrDefault(home, "player-placeholder.jpg");
                String awayLogo = logoMap.getOrDefault(away, "player-placeholder.jpg");

                Map<String, String> match = new HashMap<>();
                match.put("teamHome", home);
                match.put("teamAway", away);
                match.put("homeScore", goalsFor);
                match.put("awayScore", goalsAgainst);
                match.put("matchDate", year + "-01-01T00:00:00");
                match.put("status", "FINISHED");
                match.put("homeLogoUrl", "/" + homeLogo);
                match.put("awayLogoUrl", "/" + awayLogo);

                result.add(match);
            }
        }

        return result;
    }
}
