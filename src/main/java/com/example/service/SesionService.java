package com.example.service;

import com.example.model.Funcionario;
import com.example.model.Paciente;
import com.example.model.Sesion;
import com.example.repository.FuncionarioRepository;
import com.example.repository.PacienteRepository;
import com.example.repository.SesionRepository;
import com.example.security.dto.SesionRequest;
import com.example.security.dto.SesionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Sesion> getAllSesiones() {
        return sesionRepository.findAll();
    }

    public Optional<Sesion> getSesionById(Long id) {
        return sesionRepository.findById(id);
    }

    public boolean verificarDisponibilidad(Long idFuncionario, LocalDate fechaSesion, LocalTime hora) {
        List<Sesion> sesionesDisponibles = sesionRepository.findByFuncionarioIdAndFechaSesion(idFuncionario, fechaSesion);
        for (Sesion sesion : sesionesDisponibles) {
            if (sesion.getHora().equals(hora)) {
                return false;
            }
        }
        return true;
    }

    public SesionResponse registrarSesion(SesionRequest sesionRequest) {

        boolean disponible = verificarDisponibilidad(sesionRequest.getIdFuncionario(), sesionRequest.getFechaSesion(), sesionRequest.getHora());

        if (!disponible) {
            throw new RuntimeException("El horario ya está ocupado");
        }


        Funcionario funcionario = funcionarioRepository.findById(sesionRequest.getIdFuncionario())
                .orElseThrow(() -> new RuntimeException("Funcionario no encontrado"));


        Paciente paciente = pacienteRepository.findById(sesionRequest.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));


        Sesion sesion = new Sesion();

        sesion.setFuncionario(funcionario);
        sesion.setPaciente(paciente);
        sesion.setNombre(sesionRequest.getNombre());
        sesion.setFechaSesion(sesionRequest.getFechaSesion());
        sesion.setHora(sesionRequest.getHora());
        sesion.setMonto(sesionRequest.getMonto());
        sesion.setMetodoPago(sesionRequest.getMetodoPago());
        sesion.setEstado(Sesion.EstadoSesion.PENDIENTE);
        sesion = sesionRepository.save(sesion);


        String psicologoUsername = funcionario.getUser().getUsername();
        String pacienteUsername = paciente.getUser().getUsername();


        return new SesionResponse(
                sesion.getId(),
                sesion.getNombre(),
                pacienteUsername,
                psicologoUsername,
                sesion.getFuncionario().getIdFuncionario(),
                sesion.getPaciente().getIdPaciente(),
                sesion.getFechaSesion(),
                sesion.getHora(),
                sesion.getMonto(),
                sesion.getMetodoPago(),
                sesion.getEstado()
        );
    }

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
