package com.example.controller;

import com.example.dto.FuncionarioPaso1Request;
import com.example.dto.FuncionarioPaso1Response;
import com.example.model.Funcionario;
import com.example.model.Paciente;
import com.example.repository.FuncionarioRepository;
import com.example.repository.PacienteRepository;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.EntityNotFoundException;
import com.example.security.jwt.JwtTokenManager;
import com.example.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private FuncionarioRepository funcionarioRepository;
    private JwtTokenManager jwtTokenManager;
    private PacienteRepository pacienteRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener funcionario por ID", description = "Este endpoint permite obtener los detalles de un funcionario por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionario encontrado"),
            @ApiResponse(responseCode = "404", description = "Funcionario no encontrado")
    })
    public ResponseEntity<Funcionario> getFuncionarioById(@PathVariable Long id) {
        Funcionario funcionario = funcionarioService.getFuncionarioById(id);
        return ResponseEntity.ok(funcionario);

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
            FuncionarioPaso1Response response = new FuncionarioPaso1Response(
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

    @GetMapping("/test-error")
    public void testError() {
        throw new EntityNotFoundException("funcionario.not.found");
    }


    @PostMapping("/paso2/{idFuncionario}")
    @Operation(summary = "Registrar usuario en paso 2")
    public ResponseEntity<Map<String, Object>> registroUsuario(
            @RequestBody RegistrationRequest registrationRequest,
            @PathVariable Long idFuncionario) {

            Locale locale = LocaleContextHolder.getLocale();

            Map<String, Object> response = funcionarioService.paso2(idFuncionario, registrationRequest, locale);
            return ResponseEntity.ok(response);

    }

    @PostMapping("/paso3/{idUsuario}")
    @Operation(summary = "Completar registro funcionario", description = "Este endpoint completa el registro del funcionario en el paso 3.")
    public ResponseEntity<Funcionario> registroFuncionario(
            @PathVariable Long idUsuario,
            @RequestBody Funcionario funcionario,
            Locale locale) {

        Funcionario paso3 = funcionarioService.paso3(idUsuario, funcionario, locale);
        return new ResponseEntity<>(paso3, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar funcionario", description = "Este endpoint permite eliminar a un funcionario dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Funcionario no encontrado")
    })
    public ResponseEntity<String> eliminarFuncionario(@PathVariable Long id, Locale locale) {
            String mensaje = funcionarioService.eliminarFuncionario(id, locale);
            return ResponseEntity.ok(mensaje);

    }

    @GetMapping("/pacientes")
    public List<Paciente> pacientes(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer", "");
        Long idPsicogologo = jwtTokenManager.getUserId(token);
        return pacienteRepository.findByFuncionario_IdFuncionario(idPsicogologo);
    }
}
