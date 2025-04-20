package com.example.service;

import com.example.model.Paciente;
import com.example.model.User;
import com.example.repository.PacienteRepository;
import com.example.security.dto.CompletarPerfilPacienteRequest;
import com.example.security.dto.PacienteResponse;
import com.example.security.jwt.JwtTokenManager;
import com.example.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompletarPerfilPacienteService {


    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Transactional
    public PacienteResponse completarPerfil(Long pacienteId, CompletarPerfilPacienteRequest request, String token) {

        String email = jwtTokenManager.getEmailFromToken(token);
        User usuario = userService.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException("Token no válido o usuario no encontrado");
        }

        Paciente paciente = pacienteRepository.findById(pacienteId).orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        if (paciente.getUser() == null || !paciente.getUser().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("El paciente no tiene un usuario asociado o los datos no coinciden");
        }



        paciente.setPerfilCompletado(true);
        pacienteRepository.save(paciente);

        PacienteResponse response = new PacienteResponse();
        response.setIdPaciente(paciente.getIdPaciente());
        response.setUsername(usuario.getUsername());
        response.setEmail(usuario.getEmail());
        response.setTelefono(paciente.getTelefono());
        response.setFechaNacimiento(paciente.getFechaNacimiento());
        response.setDocumento(paciente.getDocumento());
        response.setEstado(paciente.getEstado());

        return response;
    }
}
