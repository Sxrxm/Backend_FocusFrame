package com.example.service;


import com.example.model.*;
import com.example.repository.FuncionarioRepository;
import com.example.repository.HistorialClinicoRepository;
import com.example.repository.PacienteRepository;
import com.example.repository.TerapiaRepository;
import com.example.security.dto.TerapiaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

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

   public Terapia crear (TerapiaRequest request, Locale locale) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException(messageSource.getMessage("funcionario.not.found : ", null,locale) + email));

        Paciente paciente = pacienteRepository.findById(request.getIdPaciente())
                 .orElseThrow(() -> new RuntimeException(messageSource.getMessage("patient.not.found", null, locale)));


        HistorialClinico historialClinico = paciente.getHistorialClinico();
        if (historialClinico == null) {
            throw new RuntimeException(messageSource.getMessage("medical.history.not.found",null, locale));
        }


        Terapia terapia = new Terapia();
        terapia.setDescripcion(request.getDescripcion());
        terapia.setFechaInicio(request.getFechaInicio());
        terapia.setFechaFin(request.getFechaFin());
        terapia.setFuncionario(funcionario);
        terapia.setPaciente(paciente);
        terapia.setHistorialClinico(historialClinico);
        terapia.setTipoTerapia(terapia.getTipoTerapia());

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
