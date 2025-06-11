package com.example.service;


import com.example.dto.CardPaciente;
import com.example.dto.SesionResponse;
import com.example.mapper.SesionMapper;
import com.example.model.*;
import com.example.repository.FuncionarioRepository;
import com.example.repository.PacienteRepository;
import com.example.repository.UserRepository;
import com.example.security.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {


    private final PacienteRepository pacienteRepository;
    private final UserRepository userRepository;
    private final SesionMapper sesionMapper;
    private final FuncionarioRepository funcionarioRepository;


    @Autowired
    public PacienteService(PacienteRepository pacienteRepository, UserRepository userRepository, SesionMapper sesionMapper, FuncionarioRepository funcionarioRepository) {
        this.pacienteRepository = pacienteRepository;
        this.userRepository = userRepository;
        this.sesionMapper = sesionMapper;
        this.funcionarioRepository = funcionarioRepository;
    }


    public List<CardPaciente> buscarPaciente(String busqueda, Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));
        Page<Paciente> pacientes = pacienteRepository.buscarPacientes(busqueda,funcionario, pageable);


        return pacientes.stream().map(p -> {
            List<Terapia> terapias = p.getTerapia();
            Date fechaHistorial = null;

            if (terapias == null || terapias.isEmpty()) {
                if (p.getHistorialClinico() != null) {
                    fechaHistorial = p.getHistorialClinico().getFechaCreacion();
                }
                HistorialClinico historialClinico = p.getHistorialClinico();


                return new CardPaciente(
                        p.getIdPaciente(),
                        p.getNombre() + " " + p.getApellido(),
                        p.getEmail(),
                        p.getTelefono(),
                        p.getEstado(),
                        historialClinico.getId(),
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

            Date fechaCreacionHistorial = null;
            if (t.getHistorialClinico() != null) {
                fechaCreacionHistorial = t.getHistorialClinico().getFechaCreacion();
            }

            HistorialClinico historialClinico = p.getHistorialClinico();

            double porcentajeTerapia = total == 0 ? 0 : (completada * 100.0) / total;

            return new CardPaciente(
                    p.getIdPaciente(),
                    p.getNombre() + " " + p.getApellido(),
                    p.getEmail(),
                    p.getTelefono(),
                    p.getEstado(),
                    historialClinico.getId(),
                    fechaCreacionHistorial,
                    (int) completada,
                    total,
                    t.getTipoTerapia() != null ? t.getTipoTerapia().name() : null,
                    porcentajeTerapia,
                    citasPendientes
            );
        }).collect(Collectors.toList());
    }



    @Transactional
    public Page<CardPaciente> cardPacientes(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));
        Page<Paciente> pacientes = pacienteRepository.findAllByFuncionario(funcionario,pageable);

        Page<CardPaciente> resultado = pacientes.map(p -> {
            List<Terapia> terapias = p.getTerapia();
            HistorialClinico historialClinico = p.getHistorialClinico();
            Date fechaHistorial = historialClinico != null ? historialClinico.getFechaCreacion() : null;

            if (terapias == null || terapias.isEmpty()) {
                return new CardPaciente(
                        p.getIdPaciente(),
                        p.getNombre() + " " + p.getApellido(),
                        p.getEmail(),
                        p.getTelefono(),
                        p.getEstado(),
                        historialClinico != null ? historialClinico.getId() : null,
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

            Date fechaCreacionHistorial = t.getHistorialClinico() != null ? t.getHistorialClinico().getFechaCreacion() : null;

            double porcentajeTerapia = total == 0 ? 0 : (completada * 100.0) / total;

            return new CardPaciente(
                    p.getIdPaciente(),
                    p.getNombre() + " " + p.getApellido(),
                    p.getEmail(),
                    p.getTelefono(),
                    p.getEstado(),
                    historialClinico != null ? historialClinico.getId() : null,
                    fechaCreacionHistorial,
                    (int) completada,
                    total,
                    t.getTipoTerapia() != null ? t.getTipoTerapia().name() : null,
                    porcentajeTerapia,
                    citasPendientes
            );
        });

        return resultado;
    }


//    @Transactional
//    public Paciente actualizarPaciente(Long id,Paciente paciente) {
//        if (pacienteRepository.existsById(id)) {
//            paciente.setIdPaciente(id);
//            return pacienteRepository.save(paciente);
//        }else {
//            throw new EntityNotFoundException("patient.not.found");
//        }
//    }

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
    public List<SesionResponse> obtenerSesionesPaciente() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Paciente paciente = pacienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("patient.not.found"));

        List<Sesion> sesiones = paciente.getSesions();
        return sesiones.stream()
                .map(sesionMapper::toResponse)
                .toList();
    }
}
