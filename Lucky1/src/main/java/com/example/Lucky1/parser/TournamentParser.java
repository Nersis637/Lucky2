package com.example.Lucky1.parser;

import com.example.Lucky1.model.Tournament;
import com.example.Lucky1.repository.TournamentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Component
public class TournamentParser {

    @Autowired
    private TournamentRepository tournamentRepository;

    private static final String API_TOKEN = "9b06ff557abc442cacf92755cd9ae07d";

    // @PostConstruct
    // public void runOnStartup() {
    //     System.out.println("🏁 Парсер турниров запускается при старте приложения...");
    //     parseAndSaveTournaments();
    // }

    /**
     * Запуск каждый день в 02:00
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void scheduleTournamentParsing() {
        System.out.println("⏰ Плановое обновление турниров в 02:00");
        parseAndSaveTournaments();
    }

    public void parseAndSaveTournaments() {
        String url = "https://api.football-data.org/v4/competitions";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.setHeader("X-Auth-Token", API_TOKEN);
            request.setHeader("Accept", "application/json");

            HttpResponse response = client.execute(request);
            InputStream content = response.getEntity().getContent();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);
            JsonNode competitions = root.get("competitions");

            if (competitions != null && competitions.isArray()) {
                for (JsonNode comp : competitions) {
                    int id = comp.get("id").asInt();
                    String name = comp.get("name").asText();
                    String level = comp.path("plan").asText(null);
                    String format = comp.path("type").asText(null);
                    String stage = null;

                    JsonNode currentSeason = comp.path("currentSeason");
                    if (currentSeason != null && currentSeason.has("currentMatchday")) {
                        stage = "Текущий тур: " + currentSeason.get("currentMatchday").asText();
                    }

                    Optional<Tournament> optional = tournamentRepository.findById(id);
                    Tournament tournament = optional.orElseGet(Tournament::new);
                    tournament.setTournamentId(id);
                    tournament.setName(name);
                    tournament.setLevel(level);
                    tournament.setFormat(format);
                    tournament.setStage(stage);

                    tournamentRepository.save(tournament);

                    System.out.printf("✔ Обновлён/добавлен турнир: %s (ID: %d)%n", name, id);
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Ошибка при парсинге турниров: " + e.getMessage());
        }
    }
}
