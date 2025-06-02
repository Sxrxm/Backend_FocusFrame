package com.example.security.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.exception.EntityNotFoundException;
import com.example.security.jwt.JwtTokenManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, JwtTokenManager jwtTokenManager, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String email, String password) {
        try {
            log.info("Intentando autenticar al usuario con correo: {}", email);

            User user = userRepository.findByEmail(email);
            if (user == null) {
                log.warn("Usuario no encontrado con correo: {}", email);
                throw new EntityNotFoundException("user.not.found");
            }

            log.info("Usuario encontrado: {}", user.getEmail());

            if (!passwordEncoder.matches(password, user.getPassword())) {
                log.warn("Contraseña incorrecta para el usuario: {}", email);
                throw new BadCredentialsException("Contraseña incorrecta");
            }

            log.info("Autenticación exitosa para el usuario: {}", email);

            return jwtTokenManager.generateToken(user);

        } catch (UsernameNotFoundException | BadCredentialsException e) {
            log.error("Error de autenticación: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error al intentar autenticar al usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error en el proceso de autenticación", e);
        }
    }
}