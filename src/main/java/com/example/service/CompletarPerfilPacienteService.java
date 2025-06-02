package com.example.service;

import com.example.dto.CompletarPerfilPacienteRequest;
import com.example.dto.PacienteResponse;
import com.example.model.Paciente;
import com.example.model.User;
import com.example.repository.PacienteRepository;
import com.example.security.exception.EntityNotFoundException;
import com.example.security.jwt.JwtTokenManager;
import com.example.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class CompletarPerfilPacienteService {

    private final PacienteRepository pacienteRepository;
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;
    private final MessageSource messageSource;

    @Autowired
    public CompletarPerfilPacienteService(PacienteRepository pacienteRepository, UserService userService, JwtTokenManager jwtTokenManager, MessageSource messageSource) {
        this.pacienteRepository = pacienteRepository;
        this.userService = userService;
        this.jwtTokenManager = jwtTokenManager;
        this.messageSource = messageSource;
    }

    @Transactional
    public PacienteResponse completarPerfil(Long pacienteId, CompletarPerfilPacienteRequest request, String token, Locale locale) {


        String email = jwtTokenManager.getEmailFromToken(token);
        User usuario = userService.findByEmail(email);

        if (usuario == null) {
            throw new EntityNotFoundException("user.not.found");
        }

        Paciente paciente = pacienteRepository.findById(pacienteId).orElseThrow(() -> new EntityNotFoundException("patient.not.found"));

        if (paciente.getUser() == null || !paciente.getUser().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("El paciente no tiene un usuario asociado o los datos no coinciden");
        }



        paciente.setPerfilCompletado(true);
        pacienteRepository.save(paciente);

        PacienteResponse response = new PacienteResponse();
        response.setUsername(usuario.getUsername());
        response.setEmail(usuario.getEmail());
        response.setTelefono(paciente.getTelefono());
        response.setFechaNacimiento(paciente.getFechaNacimiento());
        response.setDocumento(paciente.getDocumento());
        response.setEstado(paciente.getEstado());

        return response;
    }
}
