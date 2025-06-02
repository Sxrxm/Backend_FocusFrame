package com.example.service;

import com.example.dto.SesionRequest;
import com.example.dto.SesionResponse;
import com.example.mapper.SesionMapper;
import com.example.model.Funcionario;
import com.example.model.Paciente;
import com.example.model.Sesion;
import com.example.model.Terapia;
import com.example.repository.FuncionarioRepository;
import com.example.repository.PacienteRepository;
import com.example.repository.SesionRepository;
import com.example.repository.TerapiaRepository;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.EntityNotFoundException;
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
    private final SesionMapper sesionMapper;

    @Autowired
    public SesionService(MessageSource messageSource, SesionRepository sesionRepository, FuncionarioRepository funcionarioRepository, TerapiaRepository terapiaRepository, PacienteRepository pacienteRepository, SesionMapper sesionMapper) {
        this.messageSource = messageSource;
        this.sesionRepository = sesionRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.terapiaRepository = terapiaRepository;
        this.pacienteRepository = pacienteRepository;
        this.sesionMapper = sesionMapper;
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

    public List<LocalTime> horasDiponiblesPSicologo(Long idFuncionario, LocalDate fecha) {
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));

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
    public SesionResponse registrarSesionPsicologo(SesionRequest sesionRequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));


        Paciente paciente = pacienteRepository.findById(sesionRequest.getIdPaciente())
                .orElseThrow(() -> new EntityNotFoundException("patient.not.found"));

        Terapia terapia = terapiaRepository.findById(sesionRequest.getIdTerapia())
                .orElseThrow(() -> new EntityNotFoundException("therapy.not.found"));

        if (!isHorarioDisponible(funcionario.getIdFuncionario(),
                sesionRequest.getFechaSesion(),
                sesionRequest.getHora())) {
            throw new BadRequestException("session.time.conflict");
        }

        Sesion sesion = sesionMapper.toEntity(sesionRequest);
        sesion.setFuncionario(funcionario);
        sesion.setPaciente(paciente);
        sesion.setTerapia(terapia);
        sesion.setEstado(Sesion.EstadoSesion.PENDIENTE);

        //String psicologoUsername = funcionario.getUser().getUsername();
        String pacienteUsername = paciente.getUser().getUsername();

        sesion = sesionRepository.save(sesion);

        return sesionMapper.toResponse(sesion);
    }

    @Transactional
    public SesionResponse agendarCitaPaciente(@RequestBody SesionRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Paciente paciente = pacienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("patient.not.found"));

        Funcionario funcionario = funcionarioRepository.findById(request.getIdPsicologo())
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));

        Terapia terapia = terapiaRepository.findById(request.getIdTerapia())
                .orElseThrow(() -> new EntityNotFoundException("therapy.not.found"));

        if (!isHorarioDisponible(request.getIdPsicologo(),
                request.getFechaSesion(),
                request.getHora())) {
            throw new BadRequestException("session.time.conflict");
        }

        Sesion sesion = sesionMapper.toEntity(request);
        sesion.setFuncionario(funcionario);
        sesion.setPaciente(paciente);
        sesion.setTerapia(terapia);
        sesion.setEstado(Sesion.EstadoSesion.PENDIENTE);



        sesion = sesionRepository.save(sesion);

        return sesionMapper.toResponse(sesion);
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
