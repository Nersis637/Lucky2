package com.example.Lucky1.controller;

import com.example.Lucky1.model.Account;
import com.example.Lucky1.model.Session;
import com.example.Lucky1.service.AccountService;
import com.example.Lucky1.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/authorization")
public class AuthorizationController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showAuthorizationPage() {
        return "authorization";
    }

    @PostMapping
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional<Account> optionalAccount = accountService.findByEmail(email);

        if (optionalAccount.isPresent()) {
            Account existingAccount = optionalAccount.get();

            if (passwordEncoder.matches(password, existingAccount.getPassword())) {
                Session sessionEntity = new Session();
                sessionEntity.setUserId(existingAccount.getUser().getId());
                sessionEntity.setSessionToken(UUID.randomUUID().toString());
                sessionEntity.setCreatedAt(LocalDateTime.now());

                sessionService.saveSession(sessionEntity);
                session.setAttribute("userId", existingAccount.getUser().getId());

                return "redirect:/home";
            }
        }

        model.addAttribute("error", "Ошибка. Проверьте email или пароль.");
        return "authorization";
    }
}