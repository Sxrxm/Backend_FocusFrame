package com.example.service;


import com.example.model.Paciente;
import com.example.model.Sesion;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.PacienteRepository;
import com.example.repository.UserRepository;
import com.example.security.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PacienteService {


    private final PacienteRepository pacienteRepository;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Autowired
    public PacienteService(PacienteRepository pacienteRepository, UserRepository userRepository, MessageSource messageSource) {
        this.pacienteRepository = pacienteRepository;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }


    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id).get();
    }

    public Page<Paciente> buscarNombre(String nombre, String email, Pageable pageable) {
        return pacienteRepository.findByNombreContainingIgnoreCaseAndEmailContainingIgnoreCase(nombre, email,pageable);
    }

    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }

    @Transactional
    public Paciente actualizarPaciente(Long id,Paciente paciente) {
        if (pacienteRepository.existsById(id)) {
            paciente.setIdPaciente(id);
            return pacienteRepository.save(paciente);
        }else {
            throw new EntityNotFoundException("patient.not.found");
        }
    }

    @Transactional
    public Paciente desactivarPaciente(Long pacienteId) {
        Optional<Paciente> pacienteExistente = pacienteRepository.findById(pacienteId);

        if(pacienteExistente.isPresent()) {
            Paciente paciente = pacienteExistente.get();
            paciente.setEstado(false);
            return pacienteRepository.save(paciente);
        } else {
            throw new EntityNotFoundException("patient.not.found");
        }
    }

    @Transactional
    public String eliminarPaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("patient.not.found"));

        pacienteRepository.delete(paciente);
        User usuario = paciente.getUser();
        if (usuario != null) {
            userRepository.delete(usuario);
        }
        return "Paciente y usuario eliminados";
    }

    @Transactional
    public List<Sesion> obtenerSesionesDePaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("patient.not.found"));

        return paciente.getSesions();
    }
}
