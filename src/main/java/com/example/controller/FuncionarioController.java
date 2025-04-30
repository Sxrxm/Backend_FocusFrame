package com.example.controller;


import com.example.model.Funcionario;
import com.example.model.User;
import com.example.repository.FuncionarioRepository;
import com.example.security.dto.FuncionarioPaso1Request;
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
    ResponseEntity<Funcionario> nombresApellidos(@RequestBody FuncionarioPaso1Request paso1) {
        if (paso1.getNombre() == null || paso1.getApellido() == null || paso1.getNombre().isEmpty() || paso1.getApellido().isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }

        Funcionario funcionario = funcionarioService.paso1(paso1);

        return ResponseEntity.ok(funcionario);
    }

    @PostMapping("/paso2/{idFuncionario}")
    ResponseEntity<RegistrationResponse> registroUsuario(@RequestBody RegistrationRequest registrationRequest, @PathVariable Long idFuncionario, Locale locale) {

        try {
            RegistrationResponse response = funcionarioService.paso2(idFuncionario, registrationRequest, locale);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(e.getMessage()));
        }
    }



    @PostMapping("/paso3/{idUsuario}")
    public ResponseEntity<Funcionario> registroFuncionario(@PathVariable Long idUsuario, @RequestBody Funcionario funcionario, Locale locale) {
        Funcionario paso3 = funcionarioService.paso3(idUsuario, funcionario, locale);
        return new ResponseEntity<>(paso3, HttpStatus.CREATED);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) {
        try {
            String mensaje = funcionarioService.eliminarFuncionario(id);
            return ResponseEntity.ok(mensaje);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
