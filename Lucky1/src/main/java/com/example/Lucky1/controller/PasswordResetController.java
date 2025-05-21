package com.example.Lucky1.controller;

import com.example.Lucky1.model.Account;
import com.example.Lucky1.repository.AccountRepository;
import com.example.Lucky1.repository.PasswordResetCodeRepository;
import com.example.Lucky1.service.PasswordResetService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final AccountRepository accountRepository;
    private final PasswordResetCodeRepository codeRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(
            PasswordResetService passwordResetService,
            AccountRepository accountRepository,
            PasswordResetCodeRepository codeRepository,
            PasswordEncoder passwordEncoder) {
        this.passwordResetService = passwordResetService;
        this.accountRepository = accountRepository;
        this.codeRepository = codeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Шаг 1: Показ страницы восстановления
    @GetMapping("/restoring-access")
    public String showRestoreAccessPage() {
        return "restoring-access";
    }

    // ✅ Шаг 2: Показ страницы ввода кода
    @GetMapping("/restoring-access-2")
    public String showCodeEntryPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "restoring-access-2";
    }

    // ✅ Шаг 3: Показ страницы смены пароля
    @GetMapping("/restoring-access-3")
    public String showNewPasswordPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "restoring-access-3";
    }

    // ✅ Отправка кода на email
    @PostMapping("/send-reset-code")
    public String sendResetCode(@RequestParam String email, Model model) {
        var codeOpt = passwordResetService.generateAndSendCode(email);
        if (codeOpt.isEmpty()) {
            return "redirect:/restoring-access?error=notfound";
        }
        return "redirect:/restoring-access-2?email=" + email;
    }

    // ✅ Проверка кода
    @PostMapping("/verify-reset-code")
    public String verifyResetCode(@RequestParam String email,
                                  @RequestParam String code) {
        if (passwordResetService.verifyCode(email, code).isPresent()) {
            return "redirect:/restoring-access-3?email=" + email;
        }
        return "redirect:/restoring-access-2?email=" + email + "&error=invalid";
    }

    // ✅ Сброс пароля
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "redirect:/restoring-access-3?email=" + email + "&error=passwords";
        }

        Optional<Account> accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isEmpty()) {
            return "redirect:/restoring-access-3?email=" + email + "&error=notfound";
        }

        Account account = accountOpt.get();
        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);

        codeRepository.findByEmail(email).ifPresent(codeRepository::delete);

        return "redirect:/authorization";
    }
}