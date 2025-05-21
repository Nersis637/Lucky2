package com.example.Lucky1.controller;

import com.example.Lucky1.model.Match;
import com.example.Lucky1.model.Team;
import com.example.Lucky1.model.Player;
import com.example.Lucky1.parser.NewsParser;
import com.example.Lucky1.dto.NewsApiResponse.Article;
import com.example.Lucky1.model.User;
import com.example.Lucky1.service.MatchService;
import com.example.Lucky1.service.UserService;
import com.example.Lucky1.repository.TeamRepository;
import com.example.Lucky1.repository.PlayerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping({"/", "/home"})
public class HomeController {

    private final UserService userService;
    private final MatchService matchService;
    private final NewsParser newsParser;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public HomeController(UserService userService,
                          MatchService matchService,
                          NewsParser newsParser,
                          TeamRepository teamRepository,
                          PlayerRepository playerRepository) {
        this.userService = userService;
        this.matchService = matchService;
        this.newsParser = newsParser;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public String showHomePage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.findById(userId);
            if (user != null) {
                model.addAttribute("user", user);
            }
        }

        List<Match> matches = matchService.findTop7MatchesForToday();
        model.addAttribute("matches", matches);

        // Основные новости (6)
        List<Article> newsList = newsParser.fetchTopHeadlines();
        model.addAttribute("newsList", newsList);

        // Обзор прессы (ещё 2)
        List<Article> pressNews = newsParser.fetchTopPress();
        model.addAttribute("pressNews", pressNews);

        // Топ-5 команд ЛЧ
        List<Team> top5ChampionsLeague = teamRepository.findChampionsLeagueRanking()
                .stream()
                .limit(5)
                .toList();
        model.addAttribute("championsLeague", top5ChampionsLeague);

        // Топ-4 бомбардира
        List<Player> topScorers = playerRepository.findTopScorers()
                .stream()
                .limit(4)
                .toList();
        model.addAttribute("topScorers", topScorers);

        return "home";
    }
}