package com.example.Lucky1.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SessionController {

    @GetMapping("/session/userId")
    public Map<String, Long> getUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Map<String, Long> response = new HashMap<>();
        response.put("userId", userId);
        return response;
    }
}