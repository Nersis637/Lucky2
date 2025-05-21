package com.example.Lucky1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PreviousMatchesPageController {

    @GetMapping("/previous-matches")
    public String showPreviousMatchesPage(@RequestParam String team1,
                                          @RequestParam String team2,
                                          Model model) {
        model.addAttribute("team1", team1);
        model.addAttribute("team2", team2);
        return "previous-matches"; // шаблон из templates/
    }
}
