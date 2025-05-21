package com.example.Lucky1.dto;

import java.util.Map;

public class PredictionResponse {
    private Map<String, Map<String, Double>> result;

    public Map<String, Map<String, Double>> getResult() {
        return result;
    }

    public void setResult(Map<String, Map<String, Double>> result) {
        this.result = result;
    }
}
