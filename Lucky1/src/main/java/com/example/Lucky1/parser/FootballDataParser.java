package com.example.Lucky1.parser;

import com.example.Lucky1.model.Match;
import com.example.Lucky1.model.MatchStatus;
import com.example.Lucky1.model.Tournament;
import com.example.Lucky1.repository.MatchRepository;
import com.example.Lucky1.repository.TournamentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Optional;

@Component
public class FootballDataParser {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    private static final String API_TOKEN = "9b06ff557abc442cacf92755cd9ae07d";

    // @PostConstruct
    // public void runOnStartup() {
    //     System.out.println("🚀 Парсер матчей запускается при старте приложения...");
    //     parseAndSaveMatches();
    // }

    @Scheduled(fixedRate = 60000) // Каждую минуту
    public void runEverySixHours() {
        System.out.println("⏱ Плановое обновление матчей...");
        parseAndSaveMatches();
    }

    public void parseAndSaveMatches() {
        LocalDate today = LocalDate.now();
        String dateFrom = today.toString();
        String dateTo = today.plusDays(3).toString();

        String url = "https://api.football-data.org/v4/matches?dateFrom=" + dateFrom + "&dateTo=" + dateTo;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.setHeader("X-Auth-Token", API_TOKEN);
            request.setHeader("Accept", "application/json");

            HttpResponse response = client.execute(request);
            InputStream content = response.getEntity().getContent();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);
            JsonNode matches = root.get("matches");

            if (matches != null && matches.isArray()) {
                for (JsonNode matchNode : matches) {
                    long apiMatchId = matchNode.path("id").asLong();

                    Optional<Match> maybe = matchRepository.findById(apiMatchId);
                    Match m = maybe.orElseGet(Match::new);

                    // Привязать вручную ID из API
                    if (m.getId() == null) {
                        m.setId(apiMatchId);
                    }

                    String utc = matchNode.get("utcDate").asText();
                    ZonedDateTime utcZoned = ZonedDateTime.parse(utc, DateTimeFormatter.ISO_DATE_TIME);
                    ZonedDateTime yekaterinburgZoned = utcZoned.withZoneSameInstant(ZoneId.of("Asia/Yekaterinburg"));
                    LocalDateTime matchDate = yekaterinburgZoned.toLocalDateTime();

                    JsonNode homeNode = matchNode.get("homeTeam");
                    JsonNode awayNode = matchNode.get("awayTeam");

                    String homeName = homeNode.path("name").asText(null);
                    String awayName = awayNode.path("name").asText(null);
                    if (homeName == null || awayName == null) continue;

                    String homeLogo = homeNode.path("crest").asText(null);
                    String awayLogo = awayNode.path("crest").asText(null);

                    MatchStatus status = convertStatus(matchNode.get("status").asText());
                    JsonNode ft = matchNode.path("score").path("fullTime");
                    int hs = ft.path("home").asInt(0);
                    int as_ = ft.path("away").asInt(0);

                    m.setTeamHome(homeName);
                    m.setTeamAway(awayName);
                    m.setMatchDate(matchDate);
                    m.setStatus(status);
                    m.setHomeScore(hs);
                    m.setAwayScore(as_);
                    m.setHomeLogoUrl(homeLogo);
                    m.setAwayLogoUrl(awayLogo);

                    int tournamentId = matchNode.path("competition").path("id").asInt();
                    tournamentRepository.findById(tournamentId).ifPresent(m::setTournament);

                    matchRepository.save(m);
                    System.out.printf("✔ Сохранён матч %s vs %s%n", homeName, awayName);
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Ошибка при парсинге матчей: " + e.getMessage());
        }
    }

    private MatchStatus convertStatus(String apiStatus) {
        return switch (apiStatus) {
            case "SCHEDULED" -> MatchStatus.SCHEDULED;
            case "IN_PLAY", "PAUSED" -> MatchStatus.ONGOING;
            case "FINISHED" -> MatchStatus.FINISHED;
            default -> MatchStatus.SCHEDULED;
        };
    }
}