package com.example.controller;


import com.example.model.HistorialClinico;
import com.example.repository.HistorialClinicoRepository;
import com.example.security.dto.HistorialClinicoDto;
import com.example.security.dto.HistorialClinicoResponse;
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
    public ResponseEntity<?> crearHistorial(@PathVariable Long idPaciente, @RequestBody HistorialClinicoDto request, Locale locale){
        try {
            HistorialClinico historial = historialClinicoService.crearHistorial(idPaciente, request, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(historial);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado");
        }
    }

    @GetMapping("/consultarHistorial/{pacienteId}")
    public ResponseEntity<?> getHistorialClinico(@PathVariable Long pacienteId, Locale locale) {
        try {
            HistorialClinicoResponse response = historialClinicoService.getHistorialClinico(pacienteId, locale);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/actualizarHistorial/{id}")
    public ResponseEntity<HistorialClinico> actualizarHistorialClinico(@PathVariable Long id,
                                                                       @RequestBody HistorialClinicoDto historialClinicoDto, Locale locale) {
        HistorialClinico historialClinico = historialClinicoService.actualizarHistorialClinico(id, historialClinicoDto, locale);
        return ResponseEntity.ok(historialClinico);
    }
}
