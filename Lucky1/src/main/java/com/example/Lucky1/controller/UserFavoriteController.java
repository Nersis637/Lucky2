package com.example.Lucky1.controller;

import com.example.Lucky1.model.Match;
import com.example.Lucky1.model.User;
import com.example.Lucky1.service.MatchService;
import com.example.Lucky1.service.UserFavoriteService;
import com.example.Lucky1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/favorites")
public class UserFavoriteController {

    private final UserFavoriteService userFavoriteService;
    private final UserService userService;
    private final MatchService matchService;

    @Autowired
    public UserFavoriteController(UserFavoriteService userFavoriteService,
                                  UserService userService,
                                  MatchService matchService) {
        this.userFavoriteService = userFavoriteService;
        this.userService = userService;
        this.matchService = matchService;
    }

    // 1) Страница «Избранные матчи»
    @GetMapping
    public String getFavorites(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/authorization";
        }
        User user = userService.getUserById(userId);
        model.addAttribute("favorites", userFavoriteService.getFavoritesForUser(user));
        return "favoriteslist";
    }

    // 2) Добавить в избранное
    @PostMapping("/{matchId}")
    @ResponseBody
    public ResponseEntity<String> addFavorite(@PathVariable Long matchId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("not logged in");
        }
        User user = userService.getUserById(userId);
        Match match = matchService.getMatchById(matchId);
        if (userFavoriteService.isFavorite(user, match)) {
            return ResponseEntity.status(409).body("already favorite");
        }
        userFavoriteService.addFavorite(user, match);
        return ResponseEntity.ok("added");
    }

    // 3) Удалить из избранного
    @DeleteMapping("/{matchId}")
    @ResponseBody
    public ResponseEntity<String> removeFavorite(@PathVariable Long matchId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("not logged in");
        }
        User user = userService.getUserById(userId);
        Match match = matchService.getMatchById(matchId);
        if (!userFavoriteService.isFavorite(user, match)) {
            return ResponseEntity.status(404).body("not favorite");
        }
        userFavoriteService.removeFavorite(user, match);
        return ResponseEntity.ok("removed");
    }
}