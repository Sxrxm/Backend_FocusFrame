package com.example.controller;


import com.example.model.NotaSesion;
import com.example.service.NotaSesionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
}
