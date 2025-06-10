package com.example.service;

import com.example.dto.ResumenSesiones;
import com.example.dto.SesionRequest;
import com.example.dto.SesionResponse;
import com.example.mapper.SesionMapper;
import com.example.model.*;
import com.example.repository.FuncionarioRepository;
import com.example.repository.PacienteRepository;
import com.example.repository.SesionRepository;
import com.example.repository.TerapiaRepository;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SesionService {


    private final SesionRepository sesionRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TerapiaRepository terapiaRepository;
    private final PacienteRepository pacienteRepository;
    private final SesionMapper sesionMapper;
    private final EmailService emailService;

    @Autowired
    public SesionService(SesionRepository sesionRepository, FuncionarioRepository funcionarioRepository, TerapiaRepository terapiaRepository, PacienteRepository pacienteRepository, SesionMapper sesionMapper, EmailService emailService) {
        this.sesionRepository = sesionRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.terapiaRepository = terapiaRepository;
        this.pacienteRepository = pacienteRepository;
        this.sesionMapper = sesionMapper;
        this.emailService = emailService;
    }

    @Value("${app.horario.inicio:9}")
    private int horaInicioTrabajo;

    @Value("${app.horario.fin:18}")
    private int horaFinTrabajo;
    public List<Sesion> getAllSesiones() {
        return sesionRepository.findAll();
    }

    public Optional<Sesion> getSesionById(Long id) {
        return sesionRepository.findById(id);
    }

    public boolean isHorarioDisponible(Long idFuncionario, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {

        List<Sesion> sesiones = sesionRepository.findByFuncionarioIdFuncionarioAndFechaSesion(idFuncionario, fecha)
                .stream()
                .filter(s -> s.getEstado() != Sesion.EstadoSesion.CANCELADA)
                .collect(Collectors.toList());

        for (Sesion sesion: sesiones) {
            LocalTime inicio = sesion.getHoraInicio();
            LocalTime fin = sesion.getHoraFin();

            if (horaInicio.isBefore(fin) && horaFin.isAfter(inicio)){
                return false;
            }
        }
        return true;
    }

    public List<LocalTime> horasDiponiblesPSicologo(Long idFuncionario, LocalDate fecha) {
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));

        List<LocalTime> horasTotales = IntStream.range(horaInicioTrabajo, horaFinTrabajo)
                .mapToObj(hora -> LocalTime.of(hora, 0)).collect(Collectors.toList());

        List<Sesion> sesionesDisponibles = sesionRepository.findByFuncionarioAndFechaSesion(funcionario, fecha)
                .stream()
                .filter(s -> s.getEstado() != Sesion.EstadoSesion.CANCELADA)
                .collect(Collectors.toList());


        List<LocalTime> horasDisponibles = new ArrayList<>();

        for (LocalTime hora : horasTotales) {
            LocalTime horaFinal = hora.plusHours(1);

            boolean ocupado = sesionesDisponibles.stream()
                    .anyMatch(sesion -> hora.isBefore(sesion.getHoraFin()) && horaFinal.isAfter(sesion.getHoraInicio()));

        if (!ocupado) {
            horasDisponibles.add(hora);
        }
        }
        return horasDisponibles;
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
                sesionRequest.getHoraInicio(),
                sesionRequest.getHoraFin())) {
            throw new BadRequestException("session.time.conflict");
        }

        Sesion sesion = sesionMapper.toEntity(sesionRequest);
        sesion.setFuncionario(funcionario);
        sesion.setPaciente(paciente);
        sesion.setTerapia(terapia);
        sesion.setNotasAdicionales(sesionRequest.getNotasAdicionales());
        sesion.setEstado(Sesion.EstadoSesion.PENDIENTE);

        sesion = sesionRepository.save(sesion);

        String asunto = "Nueva sesión agendada";
        String cuerpo = "Hola " + sesion.getPaciente().getNombre() + " " + sesion.getPaciente().getApellido() +
                ", tu sesión ha sido agendada para el " + sesion.getFechaSesion()+ " en el horario: " + sesion.getHoraInicio() + " " + sesion.getHoraFin() +
                " , por el psicólogo " + sesion.getFuncionario().getNombre() + " " + sesion.getFuncionario().getApellido();

        emailService.enviarCorreoAgendamiento(paciente.getEmail(), asunto, cuerpo);

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

        if (request.getHoraFin().isBefore(request.getHoraInicio()) ||
                request.getHoraFin().equals(request.getHoraInicio())) {
            throw new BadRequestException("session.time.invalid.range");
        }

        Duration duracion = Duration.between(request.getHoraInicio(), request.getHoraFin());
        if (duracion.toMinutes() < 60) {
            throw new BadRequestException("session.time.too.short");
        }

        if (!isHorarioDisponible(request.getIdPsicologo(),
                request.getFechaSesion(),
                request.getHoraInicio(),
                request.getHoraFin())) {
            throw new BadRequestException("session.time.conflict");
        }

        Sesion sesion = sesionMapper.toEntity(request);
        sesion.setFuncionario(funcionario);
        sesion.setPaciente(paciente);
        sesion.setTerapia(terapia);
        sesion.setHistorialClinico(terapia.getHistorialClinico());
        sesion.setEstado(Sesion.EstadoSesion.PENDIENTE);
        terapia.setEstado(Terapia.EstadoTerapia.EN_PROGRESO);

        sesion = sesionRepository.save(sesion);

        String asunto = "Nueva sesión agendada";
        String cuerpo = "Hola " + sesion.getFuncionario().getNombre() + " " + sesion.getFuncionario().getApellido() +
                ", tu sesión ha sido agendada para el " + sesion.getFechaSesion()+ " en el horario: " + sesion.getHoraInicio() + " " + sesion.getHoraFin() +
                " , por el paciente " + sesion.getPaciente().getNombre() + " " + sesion.getPaciente().getApellido();

        emailService.enviarCorreoAgendamiento(paciente.getEmail(), asunto, cuerpo);


        return sesionMapper.toResponse(sesion);
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

    @Transactional
    public List<SesionResponse> obtenerSesionesPsicologo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));

        List<Sesion> sesiones = funcionario.getSesiones();
        return sesiones.stream()
                .map(sesionMapper::toResponse)
                .toList();
    }


    @Transactional
    public Sesion reagendarSesion(Long id, Sesion sesionDetails) {
        return sesionRepository.findById(id).map(sesion -> {
            sesion.setFechaSesion(sesionDetails.getFechaSesion());
            sesion.setHoraInicio(sesionDetails.getHoraInicio());
            sesion.setHoraFin(sesionDetails.getHoraFin());
            sesion.setEstado(Sesion.EstadoSesion.PENDIENTE);
            return sesionRepository.save(sesion);
        }).orElseThrow(() -> new EntityNotFoundException("session.not.found"));
    }



    @Transactional
    public void finalizarSesion(Long idSesion){
        Sesion sesion = sesionRepository.findById(idSesion).orElseThrow(() -> new EntityNotFoundException("session.not.found"));

        sesion.setEstado(Sesion.EstadoSesion.FINALIZADA);
    }

    @Transactional
    public void cancelarSesion(Long id) {
       Sesion sesion = sesionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("session.not.found"));

        if (sesion.getEstado() == Sesion.EstadoSesion.CANCELADA) {
            throw new BadRequestException("session.already.cancelled");
        }

        sesion.setEstado(Sesion.EstadoSesion.CANCELADA);
    }

    public List<ResumenSesiones> getSesionesByEstado()  { String email = SecurityContextHolder.getContext().getAuthentication().getName();

    Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));

    return sesionRepository.resumenSesiones(funcionario);
}
}
