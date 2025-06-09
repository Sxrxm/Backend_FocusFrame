package com.example.controller;

import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.UserRepository;
import com.example.security.dto.*;
import com.example.security.exception.EntityNotFoundException;
import com.example.security.service.AuthenticationService;
import com.example.security.service.PasswordResetService;
import com.example.security.service.UserService;
import com.example.security.service.UserServiceImpl;
import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordResetService passwordResetService;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;


    public AuthController(AuthenticationService authenticationService, UserService userService, UserRepository userRepository, PasswordResetService passwordResetService, UserServiceImpl userServiceImpl) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordResetService = passwordResetService;
        this.userServiceImpl = userServiceImpl;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
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

            UserRole rol = user.getUserRole();


            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }


            return ResponseEntity.ok(new LoginResponse(token, username, rol));

        } catch (BadCredentialsException e) {
            log.error("Credenciales incorrectas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception e) {
            log.error("Error interno en el proceso de login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/register")
    ResponseEntity<RegistrationResponse> register(@RequestBody @Valid RegistrationRequest registrationRequest, Locale locale) {
        try {
            RegistrationResponse response = userService.registration(registrationRequest, locale);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(e.getMessage()));
        }
    }


    @PostMapping("/recuperarContra")
    public ResponseEntity<?> forgotPassword(@RequestParam String email,
                                            Locale locale) {
        User usuario = userRepository.findByEmail(email);
        if (usuario == null) {
            String mensaje = messageSource.getMessage("email.not.found", null, locale);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
        }

        try {
            passwordResetService.recuperarPassword(email, locale);
            return ResponseEntity.ok().body(Map.of("message", "Se ha enviado un correo con instrucciones"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<?> restablecerContra(@RequestParam String token, @RequestBody RecuperarContraRequest request, Locale locale) {

        if (!request.getNueva().equals(request.getConfirmar())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Las contrasenas no coinciden.");
        }

        try {
            passwordResetService.cambiarPassword(token, request.getNueva());
            String mensaje = messageSource.getMessage("password.changed.success", null, locale);
            return ResponseEntity.ok().body(Map.of("mensaje", mensaje));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/cambioContra")
    public ResponseEntity<?> cambiarContra(@RequestBody CambioContraRequest request, Locale locale) {
        User usuario = userRepository.findByEmail(request.getEmail());
        if (usuario == null) {
            String mensaje = messageSource.getMessage("user.not.found", null, locale);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
        }

        if (!passwordEncoder.matches(request.getActual(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Contrasena actual incorrecta.");
        }

        if (!request.getNueva().equals(request.getConfirmar())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Las contrasenas no coinciden.");
        }

        usuario.setPassword(passwordEncoder.encode(request.getNueva()));
        userRepository.save(usuario);
        return ResponseEntity.ok("Contraseña cambiada con exito.");
    }


    @PatchMapping("/subirFoto/{id}")
    public User subirFoto(@PathVariable Long id, MultipartFile foto) throws IOException {
        return userServiceImpl.saveFotoPerfil(id, foto);
    }


    @GetMapping("/verFoto/{id}")
    public ResponseEntity<UrlResource> verFoto(@PathVariable Long id) {
        try {
            // Obtener el archivo desde el servicio
            File fotoPerfil = userServiceImpl.getFotoPerfil(id);

            // Crear un recurso desde el archivo
            Path path = fotoPerfil.toPath();
            UrlResource resource = new UrlResource(path.toUri());

            // Validar si el recurso es legible
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("No se pudo leer la imagen: " + fotoPerfil.getName());
            }

            // Determinar el tipo de contenido (por ejemplo, image/png)
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Construir y devolver la respuesta
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fotoPerfil.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }


    @PatchMapping("/actualizarFoto/{id}")
    public ResponseEntity<User> actualizarFoto(@PathVariable Long id, @RequestParam("nuevafoto") MultipartFile nuevafoto) throws IOException {
        User userActualizado = userServiceImpl.updateFotoPerfil(id, nuevafoto);
        return ResponseEntity.ok(userActualizado);
    }




    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
        try {
            passwordResetService.validarToken(token);
            return ResponseEntity.ok().body(
                    Map.of(
                            "valid", true,
                            "message", "Token valido",
                            "token", token
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "valid", false,
                            "error", e.getMessage(),
                            "message", "Por favor solicita un nuevo enlace de recuperación"
                    )
            );
        }
    }
}
