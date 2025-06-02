package com.example.service;

import com.example.dto.RegistroPacienteRequest;
import com.example.model.Paciente;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.PacienteRepository;
import com.example.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

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
            throw new IllegalArgumentException(messageSource.getMessage("email.use", null, locale));
        }

        if (pacienteRepository.findByDocumento(request.getDocumento()) != null) {
            throw new IllegalArgumentException(messageSource.getMessage("doc.register", null, locale));
        }

        String telefono = String.valueOf(request.getTelefono());
        String documento = String.valueOf(request.getDocumento());

        if (telefono == null || !telefono.matches("\\d{10}")) {
            throw new RuntimeException(messageSource.getMessage("phone.length.invalid", null, locale));
        }


        if (documento == null || !documento.matches("\\d{6,11}")) {
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
        return userRepository.save(usuario);
    }

    private Paciente crearPaciente(RegistroPacienteRequest request, User usuario) {
        usuario.setUserRole(UserRole.PACIENTE);

        Paciente paciente = new Paciente();
        paciente.setTelefono(request.getTelefono());
        paciente.setTipoDoc(request.getTipoDoc());
        paciente.setDocumento(request.getDocumento());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setEmail(request.getEmail());
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setFechaCreacion(new Date());
        paciente.setUser(usuario);
        paciente.setEstado(false);
        paciente.setPerfilCompletado(false);
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public Paciente registrarPaciente(RegistroPacienteRequest request, Locale locale) {
        validateRequest(request, locale);

        User usuario = createUser(request);
        Paciente paciente = crearPaciente(request, usuario);

        String enlace = "http://localhost:5173/register/" + paciente.getIdPaciente();
        emailService.enviarCorreoConEnlace(paciente.getEmail(), enlace, usuario, paciente);

        return paciente;
    }
}
