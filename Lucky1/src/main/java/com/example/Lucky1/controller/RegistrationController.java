package com.example.Lucky1.controller;

import com.example.Lucky1.model.Account;
import com.example.Lucky1.model.User;
import com.example.Lucky1.service.AccountService;
import com.example.Lucky1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showRegistrationPage() {
        return "registration";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestBody Account account) {
        try {
            logger.info("Попытка регистрации пользователя с email: {}", account.getEmail());

            // Создание нового пользователя
            User user = new User();
            user.setUsername(account.getLogin());
            userService.saveUser(user);

            // Установка пользователя и хеширование пароля
            account.setUser(user);
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            accountService.saveAccount(account);

            logger.info("Регистрация прошла успешно для email: {}", account.getEmail());
            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            logger.error("Ошибка при регистрации пользователя с email: {}", account.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка регистрации");
        }
    }
}
