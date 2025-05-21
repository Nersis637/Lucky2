package com.example.Lucky1.controller;

import com.example.Lucky1.model.ForecastChat;
import com.example.Lucky1.service.ForecastChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForecastPageController {

    @Autowired
    private ForecastChatService forecastChatService;

    @GetMapping("/forecast/chat")
    public String showForecastChat(
            @RequestParam("team1Id") Long team1Id,
            @RequestParam("team2Id") Long team2Id,
            Model model,
            HttpSession session
    ) {
        // Получаем (или создаём) чат между двумя командами
        ForecastChat chat = forecastChatService.getOrCreateChat(team1Id, team2Id);

        // Получаем userId из сессии
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            // если пользователь не авторизован — редирект на логин (или другой путь)
            return "redirect:/authorization";
        }

        // Передаём chatId и userId в шаблон
        model.addAttribute("chatId", chat.getChatId());
        model.addAttribute("userId", userId);

        return "chat"; // возвращает chat.html из src/main/resources/templates
    }
}
