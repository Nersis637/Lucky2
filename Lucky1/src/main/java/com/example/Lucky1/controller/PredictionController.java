package com.example.Lucky1.controller;

import com.example.Lucky1.dto.PredictionRequest;
import com.example.Lucky1.service.PredictionService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping
    public Map<String, Map<String, Double>> predict(@RequestBody PredictionRequest request) {
        return predictionService.getPrediction(request.getTeam1(), request.getTeam2());
    }
}
