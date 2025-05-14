package com.example.controller;


import com.example.model.Terapia;
import com.example.security.dto.TerapiaRequest;
import com.example.security.utils.GlobalExceptionHandler;
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


    @GetMapping("/Paciente/{idPaciente}/terapias")
    public ResponseEntity<?> terapiasDelPaciente(@PathVariable Long idPaciente, Locale locale) {
        try {
            Set<Terapia> terapias = terapiaService.getTerapiasPaciente(idPaciente, locale);
            return ResponseEntity.ok(terapias);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }
    @PostMapping("/crear")
    public ResponseEntity<?> crearTerapia(@RequestBody TerapiaRequest request, Locale locale) {
        try {
            Terapia terapia = terapiaService.crear(request, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(terapia);
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }
}
