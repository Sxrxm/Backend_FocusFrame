package com.example.service;


import com.example.model.Paciente;
import com.example.model.Sesion;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.PacienteRepository;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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

    public List<Paciente> buscarNombre(String nombre, Pageable pageable) {
        return pacienteRepository.findByNombreContainingIgnoreCase(nombre);
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
            throw new RuntimeException("Paciente no encontrado.");
        }
    }

    @Transactional
    public Paciente desactivarPaciente(Long pacienteId, Locale locale) {
        Optional<Paciente> pacienteExistente = pacienteRepository.findById(pacienteId);

        if(pacienteExistente.isPresent()) {
            Paciente paciente = pacienteExistente.get();
            paciente.setEstado(false);
            return pacienteRepository.save(paciente);
        } else {
            throw new EntityNotFoundException(messageSource.getMessage("patient.not.found", null, locale));
        }
    }

    @Transactional
    public String eliminarPaciente(Long pacienteId, Locale locale) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("patient.not.found", null,locale)));

        pacienteRepository.delete(paciente);
        User usuario = paciente.getUser();
        if (usuario != null) {
            userRepository.delete(usuario);
        }
        return "Paciente y usuario eliminados";
    }

    @Transactional
    public List<Sesion> obtenerSesionesDePaciente(Long pacienteId, Locale locale) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("patient.not.found", null,locale)));

        return paciente.getSesions();
    }
}
