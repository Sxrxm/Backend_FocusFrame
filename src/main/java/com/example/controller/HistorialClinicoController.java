package com.example.controller;


import com.example.dto.HistorialClinicoDto;
import com.example.dto.HistorialClinicoResponse;
import com.example.model.HistorialClinico;
import com.example.repository.HistorialClinicoRepository;
import com.example.security.service.UserServiceImpl;
import com.example.service.HistorialClinicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/historialClinico")
@CrossOrigin(origins = "http://localhost:5173")
public class HistorialClinicoController {

    private final HistorialClinicoService historialClinicoService;
    private final HistorialClinicoRepository historialClinicoRepository;
    private final UserServiceImpl userService;

    public HistorialClinicoController(HistorialClinicoService historialClinicoService, HistorialClinicoRepository historialClinicoRepository, UserServiceImpl userService) {
        this.historialClinicoService = historialClinicoService;
        this.historialClinicoRepository = historialClinicoRepository;
        this.userService = userService;
    }

    @PostMapping("/crearHistorial/{idPaciente}")
    @PreAuthorize("hasRole('PSICOLOGO')")
    public ResponseEntity<?> crearHistorial(@PathVariable Long idPaciente, @RequestBody HistorialClinicoDto request){

            HistorialClinico historial = historialClinicoService.crearHistorial(idPaciente, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(historial);
    }

    @GetMapping("/consultarHistorial/{pacienteId}")
    public ResponseEntity<?> getHistorialClinico(@PathVariable Long pacienteId) {

            HistorialClinicoResponse response = historialClinicoService.getHistorialClinico(pacienteId);
            return ResponseEntity.ok(response);

    }

    @PutMapping("/actualizarHistorial/{id}")
    public ResponseEntity<HistorialClinico> actualizarHistorialClinico(@PathVariable Long id,
                                                                       @RequestBody HistorialClinicoDto historialClinicoDto) {
        HistorialClinico historialClinico = historialClinicoService.actualizarHistorialClinico(id, historialClinicoDto);
        return ResponseEntity.ok(historialClinico);
    }
}
