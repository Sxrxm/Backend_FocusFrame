package com.example.controller;


import com.example.model.HistorialClinico;
import com.example.repository.HistorialClinicoRepository;
import com.example.security.dto.HistorialClinicoDto;
import com.example.service.HistorialClinicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/historialClinico")
public class HistorialClinicoController {

    private final HistorialClinicoService historialClinicoService;
    private final HistorialClinicoRepository historialClinicoRepository;

    public HistorialClinicoController(HistorialClinicoService historialClinicoService, HistorialClinicoRepository historialClinicoRepository) {
        this.historialClinicoService = historialClinicoService;
        this.historialClinicoRepository = historialClinicoRepository;
    }

    @PostMapping("/crearHistorial/{idPaciente}")
    public ResponseEntity<HistorialClinico> crearHistorial(@PathVariable Long idPaciente, @RequestBody HistorialClinicoDto request, Locale locale){
        try {
            HistorialClinico historial = historialClinicoService.crearHistorial(idPaciente, request, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(historial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping("/consultarHistorial/{pacienteId}")
    public ResponseEntity<HistorialClinicoDto> getHistorialClinico(@PathVariable Long pacienteId, Locale locale) {
        HistorialClinicoDto historialClinicoDto = historialClinicoService.getHistorialClinico(pacienteId, locale);
        return ResponseEntity.ok(historialClinicoDto);
    }

    @PutMapping("/actualizarHistorial/{id}")
    public ResponseEntity<HistorialClinico> actualizarHistorialClinico(@PathVariable Long id,
                                                                       @RequestBody HistorialClinicoDto historialClinicoDto, Locale locale) {
        HistorialClinico historialClinico = historialClinicoService.actualizarHistorialClinico(id, historialClinicoDto, locale);
        return ResponseEntity.ok(historialClinico);
    }
}
