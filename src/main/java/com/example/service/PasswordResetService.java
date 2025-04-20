package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;


@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    @Value("${app.baseUrl}")
    private String baseUrl;

    public void recuperarPassword(String email, Locale locale) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException(messageSource.getMessage("email.not.found", null, locale));
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);


        String linkCambio = baseUrl + "/recuperar-contrasena?token=" + token;
        String mensaje = messageSource.getMessage(
                "password.reset.email.text",
                new Object[]{linkCambio},
                locale
        );
        SimpleMailMessage correo = new SimpleMailMessage();
        correo.setTo(user.getEmail());
        String subject = messageSource.getMessage(
                "password.reset.email.subject",
                null,
                locale
        );        correo.setText(mensaje);
        mailSender.send(correo);
    }

    public void validarToken(String token) {
        User usuario = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token no válido."));

        if (usuario.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado.");
        }
    }

    public void cambiarPassword(String token, String nueva) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido."));

        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado.");
        }

        user.setPassword(passwordEncoder.encode(nueva));
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);
    }
}
