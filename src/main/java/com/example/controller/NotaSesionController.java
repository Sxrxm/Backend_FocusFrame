package com.example.controller;


import com.example.model.Funcionario;
import com.example.model.NotaSesion;
import com.example.security.exception.EntityNotFoundException;
import com.example.service.NotaSesionService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/notas")
@CrossOrigin(origins = "http://localhost:5173")

public class NotaSesionController {

    private final NotaSesionService notaSesionService;

    public NotaSesionController(NotaSesionService notaSesionService) {
        this.notaSesionService = notaSesionService;
    }


    @PostMapping("/crear")
    public NotaSesion crearNota(@RequestBody NotaSesion notaSesion){
        return notaSesionService.crearNota(notaSesion);
    }


    @GetMapping("/obtener")
    public ResponseEntity<List<NotaSesion>> obtenerNotas() {
        return  notaSesionService.obtenerNotasPorPsicologo();
    }


    @GetMapping("/obtenerPorCategoria")
    public ResponseEntity<List<NotaSesion>> obtenerNotas(@RequestParam NotaSesion.CategoriaNota categoria) {
        return  notaSesionService.obtenerNotasPorCategoria(categoria);
    }




    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizarNota(@PathVariable Long id, @RequestBody NotaSesion nuevaNota) {
        notaSesionService.actualizarNota(id, nuevaNota);
        return ResponseEntity.ok("Nota actualizada correctamente");
    }

    @DeleteMapping("/eliminar/{idNota}")
    public ResponseEntity<String> eliminarNota(@PathVariable Long idNota){
        notaSesionService.eliminarNota(idNota);
        return ResponseEntity.ok("Nota eliminada.");
    }
}
