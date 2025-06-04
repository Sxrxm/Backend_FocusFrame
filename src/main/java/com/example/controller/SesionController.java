package com.example.controller;


import com.example.dto.SesionRequest;
import com.example.dto.SesionResponse;
import com.example.model.Paciente;
import com.example.model.Sesion;
import com.example.repository.SesionRepository;
import com.example.security.jwt.JwtAuthenticationFilter;
import com.example.service.PacienteService;
import com.example.service.SesionService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequestMapping("/sesion")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class SesionController {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final PacienteService pacienteService;

    @Autowired
    private SesionService sesionService;

    @Autowired
    private SesionRepository sesionRepository;

    public SesionController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/{id}")
    public Optional<Sesion> getSesionById(@PathVariable Long id) {
        return sesionService.getSesionById(id);
    }

    @GetMapping("horariosDisponibles/{idFuncionario}/{fecha}")
    public ResponseEntity<List<LocalTime>> horasDisponibles(@PathVariable Long idFuncionario, @PathVariable LocalDate fecha) {
        List<LocalTime> disponibles = sesionService.horasDiponiblesPSicologo(idFuncionario, fecha);
        return ResponseEntity.ok(disponibles);
    }

    @PostMapping("/psicologo")
    public ResponseEntity<?> registrarSesionComoPsicologo(@RequestBody SesionRequest request) {
            SesionResponse response = sesionService.registrarSesionPsicologo(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
//    @GetMapping("/buscarPacienteEnCita")
//    public List<Paciente> filtrarPorNombreEmail(@RequestParam String busqueda) {
//        return pacienteService.buscarNombre(busqueda);
//    }

    @PostMapping("/paciente")
    public ResponseEntity<?> agendarSesionComoPaciente(@RequestBody SesionRequest request) {
            SesionResponse response = sesionService.agendarCitaPaciente(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/horas-disponibles")
    public ResponseEntity<List<LocalTime>> consultarHorasDisponibles(
            @RequestParam Long idPsicologo,
            @RequestParam String fecha
    ) {
        LocalDate localDate = LocalDate.parse(fecha);
        List<LocalTime> horas = sesionService.horasDiponiblesPSicologo(idPsicologo, localDate);
        return ResponseEntity.ok(horas);
    }

    @PutMapping("/reagendarSesion/{id}")
    public Sesion updateSesion(@PathVariable Long id, @RequestBody Sesion sesionDetails) {
        return sesionService.reagendarSesion(id, sesionDetails);
    }



    @DeleteMapping("/cancelarSesion/{id}")
    public void deleteSesion(@PathVariable Long id) {
        sesionService.cancelarSesion(id);
    }



}