package com.example.service;

import com.example.model.Paciente;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.PacienteRepository;
import com.example.repository.UserRepository;
import com.example.security.dto.RegistroPacienteRequest;
import com.example.security.jwt.JwtTokenManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Service
public class RegistroPacienteService {

    private final UserRepository userRepository;
    private final PacienteRepository pacienteRepository;
    private final EmailService emailService;
    private final MessageSource messageSource;

    @Autowired
    public RegistroPacienteService(UserRepository userRepository, PacienteRepository pacienteRepository, EmailService emailService, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.pacienteRepository = pacienteRepository;
        this.emailService = emailService;
        this.messageSource = messageSource;
    }

    public void validateRequest(RegistroPacienteRequest request, Locale locale) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException(messageSource.getMessage("email.use", null, locale ));
        }
        if (pacienteRepository.findByDocumento(request.getDocumento()) != null) {
            throw new IllegalArgumentException(messageSource.getMessage("doc.register", null,locale));
        }

        if (request.getTelefono() < 8) {
            throw new RuntimeException(messageSource.getMessage("document.length.invalid",null,locale));
        }
        if (request.getTelefono() > 11) {
            throw new RuntimeException(messageSource.getMessage("phone.length.invalid", null,locale));
        }

        if (request.getDocumento() > 11) {
            throw new RuntimeException(messageSource.getMessage("doc.length.invalid", null, locale));
        }
    }

    private User createUser(RegistroPacienteRequest request) {
        User usuario = new User();
        usuario.setUserRole(UserRole.PACIENTE);
        usuario.setEmail(request.getEmail());
        usuario.setTipoDoc(request.getTipoDoc());
        usuario.setPassword(null);
        usuario.setUsername(null);
        usuario = userRepository.save(usuario);
        return usuario;
    }

    private Paciente crearPaciente(RegistroPacienteRequest request, User usuario) {
        Paciente paciente = new Paciente();
        paciente.setTelefono(request.getTelefono());
        paciente.setTipoDoc(request.getTipoDoc());
        paciente.setDocumento(request.getDocumento());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setEmail(request.getEmail());
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setUserRole(UserRole.PACIENTE);
        paciente.setFechaCreacion(new Date());
        paciente.setUser(usuario);
        paciente.setEstado(false);
        paciente.setPerfilCompletado(false);
        paciente = pacienteRepository.save(paciente);

        return paciente;
    }


    @Transactional
    public Paciente registrarPaciente(RegistroPacienteRequest request, Locale locale) {

        validateRequest(request, locale);

        User usuario = createUser(request);
        usuario = userRepository.save(usuario);

        Paciente paciente = crearPaciente(request, usuario);
        paciente = pacienteRepository.save(paciente);

        String enlace = "http://localhost:5173/register/" + paciente.getIdPaciente();
        emailService.enviarCorreoConEnlace(paciente.getEmail(), enlace, usuario, paciente );

        return paciente;
    }


}
