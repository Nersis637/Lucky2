package com.example.Lucky1.service;

import com.example.Lucky1.dto.PredictionRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PredictionService {

    private static final String FLASK_API_URL = "http://localhost:5000/predict";

    public Map<String, Map<String, Double>> getPrediction(String team1, String team2) {
        RestTemplate restTemplate = new RestTemplate();

        PredictionRequest request = new PredictionRequest();
        request.setTeam1(team1);
        request.setTeam2(team2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PredictionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(FLASK_API_URL, entity, Map.class);

        return response.getBody();
    }
}
