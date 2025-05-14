package com.example.controller;

import com.example.model.Funcionario;
import com.example.security.dto.FuncionarioPaso1Request;
import com.example.security.dto.FuncionarioPaso1Response;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import com.example.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/funcionario")
@CrossOrigin(origins = "http://localhost:5173")
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener funcionario por ID", description = "Este endpoint permite obtener los detalles de un funcionario por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionario encontrado"),
            @ApiResponse(responseCode = "404", description = "Funcionario no encontrado")
    })
    public ResponseEntity<Funcionario> getFuncionarioById(@PathVariable Long id) {
        Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(id);
        if (funcionario.isPresent()) {
            return ResponseEntity.ok(funcionario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/paso1")
    @Operation(summary = "Registrar paso 1", description = "Este endpoint permite registrar los datos del funcionario en el primer paso del registro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en el registro")
    })
    public ResponseEntity<?> nombresApellidos(@RequestBody FuncionarioPaso1Request paso1, Locale locale) {
        try {
            Funcionario funcionario = funcionarioService.paso1(paso1, locale);
            FuncionarioPaso1Response  response = new FuncionarioPaso1Response(
                    funcionario.getIdFuncionario(),
                    funcionario.getNombre(),
                    funcionario.getApellido(),
                    funcionario.getFechaCreacion()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(e.getMessage()));
        }
    }

    @PostMapping("/paso2/{idFuncionario}")
    @Operation(summary = "Registrar usuario en paso 2")
    public ResponseEntity<Map<String, Object>> registroUsuario(
            @RequestBody RegistrationRequest registrationRequest,
            @PathVariable Long idFuncionario,
            Locale locale) {
        try {
            Map<String, Object> response = funcionarioService.paso2(idFuncionario, registrationRequest, locale);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error inesperado al registrar usuario.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/paso3/{idUsuario}")
    @Operation(summary = "Completar registro funcionario", description = "Este endpoint completa el registro del funcionario en el paso 3.")
    public ResponseEntity<?> registroFuncionario(
            @PathVariable Long idUsuario,
            @RequestBody Funcionario funcionario,
            Locale locale) {
        try {
            Funcionario paso3 = funcionarioService.paso3(idUsuario, funcionario, locale);
            return new ResponseEntity<>(paso3, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RegistrationResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar funcionario", description = "Este endpoint permite eliminar a un funcionario dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Funcionario no encontrado")
    })
    public ResponseEntity<String> eliminarFuncionario(@PathVariable Long id, Locale locale) {
        try {
            String mensaje = funcionarioService.eliminarFuncionario(id, locale);
            return ResponseEntity.ok(mensaje);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
