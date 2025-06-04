package com.example.service;


import com.example.dto.CardPaciente;
import com.example.model.*;
import com.example.repository.PacienteRepository;
import com.example.repository.TerapiaRepository;
import com.example.repository.UserRepository;
import com.example.security.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {


    private final PacienteRepository pacienteRepository;
    private final UserRepository userRepository;
    private final TerapiaRepository terapiaRepository;

    @Autowired
    public PacienteService(PacienteRepository pacienteRepository, UserRepository userRepository, TerapiaRepository terapiaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.userRepository = userRepository;
        this.terapiaRepository = terapiaRepository;
    }


    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id).get();
    }

    public List<Paciente> buscarNombre(String busqueda) {

        return pacienteRepository.buscarPacientes(busqueda);
    }


//    public List<Paciente> buscarNombreEnCita(String busqueda) {
//        return pacienteRepository.buscarPacientes(busqueda);
//    }

    @Transactional
    public List<CardPaciente> cardPacientes() {
        List<Paciente> pacientes = pacienteRepository.findAll();

        return pacientes.stream().map(p -> {
            List<Terapia> terapias = p.getTerapia();
            Date fechaHistorial;

            if (terapias == null || terapias.isEmpty()) {
                fechaHistorial = p.getHistorialClinico().getFechaCreacion();

                return new CardPaciente(
                        p.getNombre() + " " + p.getApellido(),
                        p.getEmail(),
                        p.getTelefono(),
                        p.getEstado(),
                        fechaHistorial,
                        0,
                        0,
                        null,
                        0.0,
                        false
                );

        }
            Terapia t = terapias.get(terapias.size() - 1);

            int total = t.getNumeroSesiones();
            long completada = t.getSesiones().stream()
                    .filter(s -> s.getEstado() == Sesion.EstadoSesion.FINALIZADA)
                    .count();

            boolean citasPendientes = t.getSesiones().stream()
                    .anyMatch(s -> s.getEstado() == Sesion.EstadoSesion.PENDIENTE
                            || s.getEstado() == Sesion.EstadoSesion.CONFIRMADA);

            Date fechaCreacionHistorial = t.getHistorialClinico() != null
                    ? t.getHistorialClinico().getFechaCreacion()
                    : null;

            double porcentajeTerapia = total == 0 ? 0 : (completada * 100.0) / total;

            return new CardPaciente(
                    p.getNombre(),
                    p.getEmail(),
                    p.getTelefono(),
                    p.getEstado(),
                    fechaCreacionHistorial,
                    (int) completada,
                    total,
                    t.getTipoTerapia().name(),
                    porcentajeTerapia,
                    citasPendientes
            );
        }).collect(Collectors.toList());
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
