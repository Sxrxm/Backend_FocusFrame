package com.example.security.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;


@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Autowired
    public PasswordResetService(UserRepository userRepository, JavaMailSender mailSender, PasswordEncoder passwordEncoder, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
    }


    @Transactional
    public void recuperarPassword(String email, Locale locale) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException(messageSource.getMessage("email.not.found", null, locale));
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);



        String linkCambio = "http://localhost:5173/auth/recuperar-contrasena?token=" + token;
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
        );
        correo.setSubject(subject);
        correo.setText(mensaje);
        mailSender.send(correo);
    }

    @Transactional
    public void validarToken(String token) {
        User usuario = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token no válido."));

        if (usuario.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado.");
        }
    }

    @Transactional
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
