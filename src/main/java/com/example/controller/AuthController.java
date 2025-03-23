package com.example.controller;

import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.UserRepository;
import com.example.security.dto.LoginRequest;
import com.example.security.dto.LoginResponse;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import com.example.security.service.AuthenticationService;
import com.example.security.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;


    public AuthController(AuthenticationService authenticationService, UserService userService, UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.info("Intentando autenticar al usuario: {}", loginRequest.getEmail());

            if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty() ||
                    loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (!isValidEmail(loginRequest.getEmail())) {
                return ResponseEntity.badRequest().build();
            }

            String token = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            User user = userRepository.findByEmail(loginRequest.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }


            String username = user.getUsername();


            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }


            return ResponseEntity.ok(new LoginResponse(token, username));

        } catch (BadCredentialsException e) {
            log.error("Credenciales incorrectas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception e) {
            log.error("Error interno en el proceso de login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/register")
    ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        try {
            RegistrationResponse response = userService.registration(registrationRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(e.getMessage()));
        }
    }


}
