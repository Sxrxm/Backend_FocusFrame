package com.example.service;


import com.example.dto.TerapiaRequest;
import com.example.model.*;
import com.example.repository.FuncionarioRepository;
import com.example.repository.PacienteRepository;
import com.example.repository.TerapiaRepository;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.EntityNotFoundException;
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

    public Set<Terapia> getTerapiasPaciente (Long idPaciente) {
        Paciente paciente =pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new EntityNotFoundException("patient.not.found"));

        HistorialClinico historialClinico = paciente.getHistorialClinico();
        if (historialClinico == null) {
            throw new EntityNotFoundException("history.not.found");
        }

        return historialClinico.getTerapias();
    }

   public Terapia crear (TerapiaRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found : "));

        Paciente paciente = pacienteRepository.findById(request.getIdPaciente())
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
}
