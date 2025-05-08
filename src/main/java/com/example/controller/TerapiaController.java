package com.example.controller;


import com.example.model.Terapia;
import com.example.security.dto.TerapiaRequest;
import com.example.service.TerapiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

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
