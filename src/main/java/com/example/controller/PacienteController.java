package com.example.controller;

import com.example.dto.CompletarPerfilPacienteRequest;
import com.example.dto.RegistroPacienteRequest;
import com.example.model.Paciente;
import com.example.model.User;
import com.example.repository.PacienteRepository;
import com.example.repository.UserRepository;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.EntityNotFoundException;
import com.example.security.jwt.JwtAuthenticationFilter;
import com.example.security.service.UserService;
import com.example.service.PacienteService;
import com.example.service.RegistroPacienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;


@RestController
@RequestMapping("/paciente")
@CrossOrigin(origins = "http://localhost:5173")
public class PacienteController {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PacienteService pacienteService;


    @Autowired
    private RegistroPacienteService registroPacienteService;


    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;



    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPaciente(@RequestBody RegistroPacienteRequest request, Locale locale) {
        try {
            Paciente paciente = registroPacienteService.registrarPaciente(request, locale);
            return new ResponseEntity<>(paciente, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            String mensaje = ex.getMessage();
            Map<String, String> error = Map.of("error", mensaje);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ex) {
            String mensaje = ex.getMessage();
            Map<String, String> error = Map.of("error", mensaje);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/completar-perfil/{pacienteId}")
    public ResponseEntity<String> completarPerfil(
            @PathVariable Long pacienteId,
            @RequestBody CompletarPerfilPacienteRequest request,
            Locale locale) {

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("patient.not.found"));

        if (paciente.isPerfilCompletado()) {
            log.error("El perfil ya está completado {}", pacienteId);
            throw new BadRequestException("perfil.ya.completado");
        }

        User usuario = userRepository.findByEmail(paciente.getEmail());

        if (usuario == null) {
            log.error("Usuario no encontrado con el email {}", paciente.getEmail());
            throw new EntityNotFoundException("user.not.found");
        }

        paciente.setEstado(true);
        paciente.setPerfilCompletado(true);
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(usuario);
        pacienteRepository.save(paciente);

        return ResponseEntity.ok("Perfil completado con éxito");
    }




    @PostMapping("/desactivar/{pacienteId}")
    public ResponseEntity<Paciente> desactivarPaciente(@PathVariable Long pacienteId) {
            Paciente pacienteDessactivado = pacienteService.desactivarPaciente(pacienteId);
            return new ResponseEntity<>(pacienteDessactivado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) {

            String mensaje = pacienteService.eliminarPaciente(id );
            return ResponseEntity.ok(mensaje);

    }


    @GetMapping("/buscarPaciente")
    public Page<Paciente> filtrarPorNombreEmail(@RequestParam String nombre,String email,  Pageable pageable) {
        return pacienteService.buscarNombre(nombre, email,pageable);
    }


    @GetMapping("/pacientes")
    public List<Paciente> getAllPacientes() {
        return pacienteService.getAllPacientes();
    }


    @PutMapping("/actualizarPaciente")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable Long id,@RequestBody Paciente paciente, Locale locale) {
        try {
            String mensaje = messageSource.getMessage("user.not.found", null,locale);
            Paciente pacienteExistente = pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException(mensaje));


            pacienteExistente.setNombre(paciente.getNombre());
            pacienteExistente.setTelefono(paciente.getTelefono());

            return ResponseEntity.ok(pacienteExistente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
