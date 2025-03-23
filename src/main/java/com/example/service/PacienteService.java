package com.example.service;


import com.example.model.Paciente;
import com.example.model.Sesion;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.PacienteRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private UserRepository userRepository;


    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id).get();
    }

    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente actualizarPaciente(Long id,Paciente paciente) {
        if (pacienteRepository.existsById(id)) {
            paciente.setIdPaciente(id);
            return pacienteRepository.save(paciente);
        }else {
            throw new RuntimeException("Paciente no encontrado.");
        }
    }

    public Paciente desactivarPaciente(Long pacienteId) {
        Optional<Paciente> pacienteExistente = pacienteRepository.findById(pacienteId);

        if(pacienteExistente.isPresent()) {
            Paciente paciente = pacienteExistente.get();
            paciente.setEstado(false);
            return pacienteRepository.save(paciente);
        } else {
            throw new RuntimeException("Paciente no encontrado");
        }
    }

    public String eliminarPaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        pacienteRepository.delete(paciente);
        User usuario = paciente.getUser();
        if (usuario != null) {
            userRepository.delete(usuario);
        }
        return "Paciente u usuario eliminados";
    }

    public List<Sesion> obtenerSesionesDePaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        return paciente.getSesions();
    }
}
