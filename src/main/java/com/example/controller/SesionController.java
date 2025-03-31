package com.example.controller;


import com.example.model.Sesion;
import com.example.repository.SesionRepository;
import com.example.security.dto.SesionRequest;
import com.example.security.dto.SesionResponse;
import com.example.security.jwt.JwtAuthenticationFilter;
import com.example.service.SesionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/sesion")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SesionController {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @Autowired
    private SesionService sesionService;

    @Autowired
    private SesionRepository sesionRepository;

    @GetMapping("/{id}")
    public Optional<Sesion> getSesionById(@PathVariable Long id) {
        return sesionService.getSesionById(id);
    }

    @PostMapping("/createSesion")
    public ResponseEntity<SesionResponse> registrarSesion(@RequestBody SesionRequest sesionRequest) {
        if (sesionRequest.getIdFuncionario() == null || sesionRequest.getIdPaciente() == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("paciente o funcionario no pueden ser nulos");
        }
        try {
            SesionResponse sesionResponse = sesionService.registrarSesion(sesionRequest);
            return new ResponseEntity<>(sesionResponse, HttpStatus.CREATED);
        }catch (Exception e) {

            log.error("Error al crear la sesión: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/updateSesion/{id}")
    public Sesion updateSesion(@PathVariable Long id, @RequestBody Sesion sesionDetails) {
        return sesionService.updateSesion(id, sesionDetails);
    }


    @DeleteMapping("/deleteSesion/{id}")
    public void deleteSesion(@PathVariable Long id) {
        sesionService.deleteSesion(id);
    }



}