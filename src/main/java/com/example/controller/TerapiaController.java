package com.example.controller;


import com.example.dto.TerapiaRequest;
import com.example.model.Terapia;
import com.example.service.TerapiaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping("/terapia")
@CrossOrigin(origins = "http://localhost:5173")

public class TerapiaController {

    @Autowired
    private MessageSource messageSource;

    private final TerapiaService terapiaService;

    public TerapiaController(TerapiaService terapiaService) {
        this.terapiaService = terapiaService;
    }


    @GetMapping("/{idPaciente}")
    public ResponseEntity<?> terapiasDelPaciente(@PathVariable Long idPaciente) {
        Set<Terapia> terapias = terapiaService.getTerapiasPaciente(idPaciente);
        return ResponseEntity.ok(terapias);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearTerapia(@RequestBody TerapiaRequest request) {
        Terapia terapia = terapiaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(terapia);
    }


    @PutMapping("/finalizar/{idTerapia}")
    public ResponseEntity<String> finalizarTerapia(@PathVariable Long idTerapia){
        terapiaService.finalizar(idTerapia);
        return ResponseEntity.ok("Terapia finalizada con éxito");
    }


    @DeleteMapping("/cancelar/{idTerapia}")
    public ResponseEntity<String> cancelarTerapia(@PathVariable Long idTerapia){
        terapiaService.cancelar(idTerapia);
        return ResponseEntity.ok("Terapia cancelada.");

    }
}
