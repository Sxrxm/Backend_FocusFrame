package com.example.service;

import com.example.dto.RegistroPacienteRequest;
import com.example.model.Funcionario;
import com.example.model.Paciente;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.FuncionarioRepository;
import com.example.repository.PacienteRepository;
import com.example.repository.UserRepository;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RegistroPacienteService {

    private final UserRepository userRepository;
    private final PacienteRepository pacienteRepository;
    private final EmailService emailService;
    private final FuncionarioRepository funcionarioRepository;

    @Autowired
    public RegistroPacienteService(UserRepository userRepository, PacienteRepository pacienteRepository, EmailService emailService, FuncionarioRepository funcionarioRepository) {
        this.userRepository = userRepository;
        this.pacienteRepository = pacienteRepository;
        this.emailService = emailService;
        this.funcionarioRepository = funcionarioRepository;
    }

    public void validateRequest(RegistroPacienteRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new BadRequestException("email.use");
        }

        if (pacienteRepository.findByDocumento(request.getDocumento()) != null) {
            throw new BadRequestException("doc.register");
        }

        String telefono = String.valueOf(request.getTelefono());
        String documento = String.valueOf(request.getDocumento());

        if (telefono == null || !telefono.matches("\\d{10}")) {
            throw new BadRequestException("phone.length.invalid");
        }


        if (documento == null || !documento.matches("\\d{6,11}")) {
            throw new BadRequestException("doc.length.invalid");
        }
    }

    private User createUser(RegistroPacienteRequest request) {
        User usuario = new User();
        usuario.setUserRole(UserRole.PACIENTE);
        usuario.setEmail(request.getEmail());
        usuario.setTipoDoc(request.getTipoDoc());
        usuario.setPassword(null);
        usuario.setUsername(null);
        return userRepository.save(usuario);
    }

    private Paciente crearPaciente(RegistroPacienteRequest request, User usuario) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));


        Paciente paciente = new Paciente();
        paciente.setTelefono(request.getTelefono());
        paciente.setTipoDoc(request.getTipoDoc());
        paciente.setDocumento(request.getDocumento());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setEmail(request.getEmail());
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setFechaCreacion(new Date());
        paciente.setUserRole(usuario.getUserRole());
        paciente.setUser(usuario);
        paciente.setEstado(false);
        paciente.setPerfilCompletado(false);
        paciente.setFuncionario(funcionario);
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public Paciente registrarPaciente(RegistroPacienteRequest request) {
        validateRequest(request);

        User usuario = createUser(request);
        Paciente paciente = crearPaciente(request, usuario);

        String enlace = "http://localhost:5173/register/" + paciente.getIdPaciente();
        emailService.enviarCorreoConEnlace(paciente.getEmail(), enlace, usuario, paciente);

        return paciente;
    }
}
