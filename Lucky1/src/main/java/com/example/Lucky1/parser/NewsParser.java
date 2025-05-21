package com.example.Lucky1.parser;

import com.example.Lucky1.dto.NewsApiResponse;
import com.example.Lucky1.dto.NewsApiResponse.Article;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class NewsParser {
    public String formatPublishedAt(String isoDate) {
        try {
            OffsetDateTime odt = OffsetDateTime.parse(isoDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", new Locale("ru"));
            return odt.format(formatter);
        } catch (Exception e) {
            return "";
        }
    }
    private final RestTemplate rest = new RestTemplate();

    @Value("${news.api.key}")
    private String apiKey;

    private static final String URL =
            "https://newsapi.org/v2/everything?q=футбол&language=ru&sortBy=publishedAt&apiKey=";

    /**
     * Возвращает первые 6 новостей для блока "Новости".
     */
    public List<Article> fetchTopHeadlines() {
        return fetchArticles(0, 6);
    }

    /**
     * Возвращает следующие 2 новости для блока "Обзор прессы".
     */
    public List<Article> fetchTopPress() {
        return fetchArticles(6, 2);
    }

    /**
     * Общий метод получения и нарезки новостей.
     */
    private List<Article> fetchArticles(int skip, int limit) {
        String fullUrl = URL + apiKey;
        ResponseEntity<NewsApiResponse> resp =
                rest.exchange(fullUrl,
                        HttpMethod.GET,
                        new HttpEntity<>(new HttpHeaders()),
                        NewsApiResponse.class);

        if (resp.getStatusCode() != HttpStatus.OK || resp.getBody() == null) {
            throw new RuntimeException("Не удалось получить новости: " + resp.getStatusCode());
        }

        NewsApiResponse body = resp.getBody();
        if (!"ok".equals(body.getStatus())) {
            throw new RuntimeException("NewsAPI вернул статус: " + body.getStatus());
        }

        return body.getArticles()
                .stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
