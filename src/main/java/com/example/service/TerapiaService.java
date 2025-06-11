package com.example.service;


import com.example.dto.TerapiaRequest;
import com.example.model.*;
import com.example.repository.FuncionarioRepository;
import com.example.repository.PacienteRepository;
import com.example.repository.TerapiaRepository;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TerapiaService {

    @Autowired
    private MessageSource messageSource;

    private final TerapiaRepository terapiaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PacienteRepository pacienteRepository;

    public TerapiaService(TerapiaRepository terapiaRepository, FuncionarioRepository funcionarioRepository, PacienteRepository pacienteRepository) {
        this.terapiaRepository = terapiaRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.pacienteRepository = pacienteRepository;
    }


    @Transactional
    public Set<Terapia> getTerapiasPaciente (Long idPaciente) {
        Paciente paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new EntityNotFoundException("patient.not.found"));

        HistorialClinico historialClinico = paciente.getHistorialClinico();
        if (historialClinico == null) {
            throw new EntityNotFoundException("history.not.found");
        }
         return historialClinico.getTerapias();
    }


    @Transactional
   public Terapia  crear ( Long idPaciente, TerapiaRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found : "));

        Paciente paciente = pacienteRepository.findById(idPaciente)
                 .orElseThrow(() -> new EntityNotFoundException("patient.not.found"));


        HistorialClinico historialClinico = paciente.getHistorialClinico();
        if (historialClinico == null) {
            throw new BadRequestException("medical.history.not.found");
        }


        Terapia terapia = new Terapia();
        terapia.setDescripcion(request.getDescripcion());
        terapia.setFechaInicio(request.getFechaInicio());
        terapia.setFechaFin(request.getFechaFin());
        terapia.setTipoTerapia(request.getTipoTerapia());
        terapia.setFuncionario(funcionario);
        terapia.setPaciente(paciente);
        terapia.setHistorialClinico(historialClinico);
        terapia.setEstado(Terapia.EstadoTerapia.EN_PROGRESO);
        terapia.setNumeroSesiones(request.getNumeroSesiones());

        terapia = terapiaRepository.save(terapia);

        if (request.getSesiones() != null) {
            for (Sesion sesion : request.getSesiones()) {
                sesion.setTerapia(terapia);
            }
            terapia.setSesiones(request.getSesiones());
            terapia = terapiaRepository.save(terapia);
        }
        return terapia;
    }


    @Transactional
    public void finalizar(Long idTerapia) {
        Terapia terapia = terapiaRepository.findById(idTerapia).orElseThrow(() -> new EntityNotFoundException("therapy.not.found"));

        terapia.setEstado(Terapia.EstadoTerapia.FINALIZADA);

    }

    @Transactional
    public void cancelar(Long idTerapia) {

        Terapia terapia = terapiaRepository.findById(idTerapia).orElseThrow(() -> new EntityNotFoundException("therapy.not.found"));


        if (terapia.getEstado() == Terapia.EstadoTerapia.CANCELADA) {
            throw new RuntimeException("terapia ya cancelada");
        }
        if (terapia.getEstado() == Terapia.EstadoTerapia.FINALIZADA) {
            throw new RuntimeException("terapia ya finalizada, no se puede cancelar");
        }

        terapia.setEstado(Terapia.EstadoTerapia.CANCELADA);
    }

}
