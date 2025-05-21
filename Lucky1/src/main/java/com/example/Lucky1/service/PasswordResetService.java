package com.example.Lucky1.service;

import com.example.Lucky1.model.Account;
import com.example.Lucky1.model.PasswordResetCode;
import com.example.Lucky1.repository.AccountRepository;
import com.example.Lucky1.repository.PasswordResetCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class PasswordResetService {

    private final PasswordResetCodeRepository codeRepository;
    private final AccountRepository accountRepository;
    private final JavaMailSender mailSender;

    public PasswordResetService(PasswordResetCodeRepository codeRepository,
                                AccountRepository accountRepository,
                                JavaMailSender mailSender) {
        this.codeRepository = codeRepository;
        this.accountRepository = accountRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public Optional<String> generateAndSendCode(String email) {
        Optional<Account> accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isEmpty()) {
            return Optional.empty(); // email не найден в базе
        }

        // Генерация кода
        String code = String.format("%06d", new Random().nextInt(1_000_000));

        // Удаляем предыдущий код, если он есть
        codeRepository.findByEmail(email).ifPresent(codeRepository::delete);

        // Сохраняем новый код
        PasswordResetCode resetCode = new PasswordResetCode(
                email,
                code,
                LocalDateTime.now().plusMinutes(10)
        );
        codeRepository.save(resetCode);

        // Отправляем письмо
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Код восстановления пароля");
        message.setText("Ваш код восстановления: " + code);
        mailSender.send(message);

        return Optional.of(code);
    }

    public Optional<PasswordResetCode> verifyCode(String email, String code) {
        return codeRepository.findByEmailAndCode(email, code)
                .filter(c -> c.getExpiration().isAfter(LocalDateTime.now()));
    }
}