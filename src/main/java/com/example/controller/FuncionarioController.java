package com.example.controller;


import com.example.model.Funcionario;
import com.example.model.User;
import com.example.repository.FuncionarioRepository;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import com.example.security.service.UserService;
import com.example.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private UserService userService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping("/{id}")
    public Optional<Funcionario> getFuncionarioById(@PathVariable Long id) {
        return funcionarioService.getFuncionarioById(id);
    }

    @GetMapping("/obtener")
    public List<Funcionario> getFuncionarios() {
        return funcionarioService.getAllFuncionario();
    }

    @PostMapping("/paso1")
    ResponseEntity<RegistrationResponse> registroUsuario(@RequestBody RegistrationRequest registrationRequest) {
        try {
            RegistrationResponse response = userService.registration(registrationRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(e.getMessage()));
        }
    }



    @PostMapping("/paso2/{idUsuario}")
    public ResponseEntity<Funcionario> registroFuncionario(@PathVariable Long idUsuario, @RequestBody Funcionario funcionario) {
        Funcionario paso2 = funcionarioService.paso2(idUsuario, funcionario);
        return new ResponseEntity<>(paso2, HttpStatus.CREATED);
    }
}
