package com.example.controller;

import com.example.dto.CardPaciente;
import com.example.dto.FuncionarioPaso1Request;
import com.example.dto.FuncionarioPaso1Response;
import com.example.dto.SesionResponse ;
import com.example.model.Funcionario;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import com.example.service.FuncionarioService;
import com.example.service.PacienteService;
import com.example.service.SesionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/funcionario")
@CrossOrigin(origins = "http://localhost:5173")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;
    private final PacienteService pacienteService;
    private final SesionService sesionService;

    public FuncionarioController(FuncionarioService funcionarioService, PacienteService pacienteService, SesionService sesionService) {
        this.funcionarioService = funcionarioService;
        this.pacienteService = pacienteService;
        this.sesionService = sesionService;
    }


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



    @Operation(
            summary = "Registrar usuario en paso 2",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación del usuario",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Funcionario no encontrado",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
            }
    )
    @PostMapping("/paso2/{idFuncionario}")
    public ResponseEntity<Map<String, Object>> registroUsuario(
            @RequestBody RegistrationRequest registrationRequest,
            @PathVariable Long idFuncionario) {

            Map<String, Object> response = funcionarioService.paso2(idFuncionario, registrationRequest);
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
    public ResponseEntity<Page<CardPaciente>> misPacientes(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idPaciente").descending());
        return ResponseEntity.ok(pacienteService.cardPacientes(pageable));
    }


    @GetMapping("/sesiones")
    public ResponseEntity<List<SesionResponse>> obtenerSesiones(){
        List<SesionResponse> sesiones = sesionService.obtenerSesionesPsicologo();
        return ResponseEntity.ok(sesiones);
    }
}
