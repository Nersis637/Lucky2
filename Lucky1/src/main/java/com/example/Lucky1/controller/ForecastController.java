package com.example.Lucky1.controller;

import com.example.Lucky1.model.ForecastChat;
import com.example.Lucky1.service.ForecastChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ForecastController {

    @Autowired
    private ForecastChatService forecastChatService;

    @GetMapping("/forecast")
    public String forecastPage() {
        return "forecast"; // шаблон forecast.html
    }

    @GetMapping("/forecast/result")
    public String forecastResult(@RequestParam String team1,
                                 @RequestParam String team2,
                                 Model model) {
        model.addAttribute("team1", team1);
        model.addAttribute("team2", team2);
        return "forecast-result"; // шаблон forecast-result.html
    }

    // 💬 Эндпоинт для получения chatId и userId
    @GetMapping("/forecast/chatId")
    @ResponseBody
    public Map<String, Object> getChatInfo(@RequestParam("team1Id") Long team1Id,
                                           @RequestParam("team2Id") Long team2Id,
                                           HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("Пользователь не авторизован");
        }

        ForecastChat chat = forecastChatService.getOrCreateChat(team1Id, team2Id);

        Map<String, Object> response = new HashMap<>();
        response.put("chatId", chat.getChatId());
        response.put("userId", userId);
        return response;
    }
}