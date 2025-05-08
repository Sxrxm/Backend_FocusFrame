package com.example.service;

import com.example.model.Funcionario;
import com.example.model.Paciente;
import com.example.model.Sesion;
import com.example.model.Terapia;
import com.example.repository.FuncionarioRepository;
import com.example.repository.PacienteRepository;
import com.example.repository.SesionRepository;
import com.example.repository.TerapiaRepository;
import com.example.security.dto.SesionRequest;
import com.example.security.dto.SesionResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SesionService {


    private final MessageSource messageSource;
    private final SesionRepository sesionRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TerapiaRepository terapiaRepository;
    private final PacienteRepository pacienteRepository;

    @Autowired
    public SesionService(MessageSource messageSource, SesionRepository sesionRepository, FuncionarioRepository funcionarioRepository, TerapiaRepository terapiaRepository, PacienteRepository pacienteRepository) {
        this.messageSource = messageSource;
        this.sesionRepository = sesionRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.terapiaRepository = terapiaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Value("${app.horario.inicio:9}")
    private int horaInicio;

    @Value("${app.horario.fin:18}")
    private int horaFin;
    public List<Sesion> getAllSesiones() {
        return sesionRepository.findAll();
    }

    public Optional<Sesion> getSesionById(Long id) {
        return sesionRepository.findById(id);
    }
    public boolean isHorarioDisponible(Long idFuncionario, LocalDate fecha, LocalTime hora) {
        return sesionRepository.countByFuncionarioIdFuncionarioAndFechaSesionAndHora(idFuncionario, fecha, hora) == 0;
    }

    public List<LocalTime> horasDiponiblesPSicologo(Long idFuncionario, LocalDate fecha, Locale locale) {
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("funcionario.not.found", null, locale)));

        List<LocalTime> horasTodas = IntStream.range(horaInicio, horaFin)
                .mapToObj(hora -> LocalTime.of(hora, 0)).collect(Collectors.toList());

        Set<LocalTime> horasOcupadas = sesionRepository.findByFuncionarioAndFechaSesion(funcionario, fecha)
                .stream()
                .map(Sesion::getHora)
                .collect(Collectors.toSet());

        return horasTodas.stream()
                .filter(hora -> !horasOcupadas.contains(hora))
                .collect(Collectors.toList());
    }

    @Transactional
    public SesionResponse registrarSesionPsicologo(SesionRequest sesionRequest,  Locale locale) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("funcionario.not.found", null, locale)));


        Paciente paciente = pacienteRepository.findById(sesionRequest.getIdPaciente())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("patient.not.found", null,locale)));

        Terapia terapia = terapiaRepository.findById(sesionRequest.getIdTerapia())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("therapy.not.found",null,locale)));

        if (!isHorarioDisponible(funcionario.getIdFuncionario(),
                sesionRequest.getFechaSesion(),
                sesionRequest.getHora())) {
            throw new RuntimeException(messageSource.getMessage("session.time.conflict", null, locale));
        }
        Sesion sesion = new Sesion();


        sesion.setFuncionario(funcionario);
        sesion.setTerapia(terapia);
        sesion.setPaciente(paciente);
        sesion.setNombre(sesionRequest.getNombre());
        sesion.setFechaSesion(sesionRequest.getFechaSesion());
        sesion.setHora(sesionRequest.getHora());
        sesion.setEstado(Sesion.EstadoSesion.PENDIENTE);
        sesion = sesionRepository.save(sesion);


        //String psicologoUsername = funcionario.getUser().getUsername();
        String pacienteUsername = paciente.getUser().getUsername();


        return new SesionResponse(
                sesion.getId(),
                sesion.getNombre(),
                paciente.getNombre(),
                funcionario.getNombre(),
                sesion.getFuncionario().getIdFuncionario(),
                sesion.getPaciente().getIdPaciente(),
                sesion.getFechaSesion(),
                sesion.getHora(),
                sesion.getEstado()
        );
    }

    @Transactional
    public SesionResponse agendarCitaPaciente(@RequestBody SesionRequest request, Locale locale) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Paciente paciente = pacienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("patient.not.found", null,locale)));

        Funcionario funcionario = funcionarioRepository.findById(request.getIdPsicologo())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("funcionario.not.found", null, locale)));

        Terapia terapia = terapiaRepository.findById(request.getIdTerapia())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("therapy.not.found",null,locale)));

        if (!isHorarioDisponible(request.getIdPsicologo(),
                request.getFechaSesion(),
                request.getHora())) {
            throw new RuntimeException(messageSource.getMessage("session.time.conflict", null, locale));
        }


        Sesion sesion = new Sesion();

        sesion.setFuncionario(funcionario);
        sesion.setTerapia(terapia);
        sesion.setPaciente(paciente);
        sesion.setNombre(request.getNombre());
        sesion.setFechaSesion(request.getFechaSesion());
        sesion.setHora(request.getHora());
        sesion.setEstado(Sesion.EstadoSesion.PENDIENTE);
        sesion = sesionRepository.save(sesion);

        String psicologoUsername = funcionario.getUser().getUsername();

        String pacienteUsername = paciente.getUser().getUsername();


        return new SesionResponse(
                sesion.getId(),
                sesion.getNombre(),
                pacienteUsername,
                funcionario.getNombre(),
                sesion.getFuncionario().getIdFuncionario(),
                sesion.getPaciente().getIdPaciente(),
                sesion.getFechaSesion(),
                sesion.getHora(),
                sesion.getEstado()
        );

   }

   @Transactional
    public Sesion updateSesion(Long id, Sesion sesionDetails) {
        return sesionRepository.findById(id).map(sesion -> {
            sesion.setFechaSesion(sesionDetails.getFechaSesion());
            sesion.setEstado(sesionDetails.getEstado());
            return sesionRepository.save(sesion);
        }).orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
    }

    public void deleteSesion(Long id) {
        sesionRepository.deleteById(id);
    }

    public List<Sesion> getSesionesByEstado(String estado) {
        return sesionRepository.findByEstado(estado);
    }
}
