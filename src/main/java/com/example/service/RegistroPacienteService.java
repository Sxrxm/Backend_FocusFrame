package com.example.service;

import com.example.model.Paciente;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.PacienteRepository;
import com.example.repository.UserRepository;
import com.example.security.dto.RegistroPacienteRequest;
import com.example.security.jwt.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Service
public class RegistroPacienteService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    MessageSource messageSource;



    public Paciente registrarPaciente(RegistroPacienteRequest registroPacienteRequest, Locale locale) {

        if (userRepository.findByEmail(registroPacienteRequest.getEmail()) != null) {
            throw new IllegalArgumentException(messageSource.getMessage("email.use", null, locale ));
        }


        if (pacienteRepository.findByDocumento(registroPacienteRequest.getDocumento()) != null) {
            throw new IllegalArgumentException(messageSource.getMessage("doc.register", null, locale));
        }



        User usuario = new User();
        usuario.setUserRole(UserRole.PACIENTE);
        usuario.setEmail(registroPacienteRequest.getEmail());
        usuario.setTipoDoc(registroPacienteRequest.getTipoDoc());
        usuario.setPassword(null);
        usuario.setUsername(null);
        usuario = userRepository.save(usuario);

        Paciente paciente = new Paciente();
        paciente.setTelefono(registroPacienteRequest.getTelefono());
        paciente.setTipoDoc(registroPacienteRequest.getTipoDoc());
        paciente.setDocumento(registroPacienteRequest.getDocumento());
        paciente.setFechaNacimiento(registroPacienteRequest.getFechaNacimiento());
        paciente.setEmail(registroPacienteRequest.getEmail());
        paciente.setNombre(registroPacienteRequest.getNombre());
        paciente.setApellido(registroPacienteRequest.getApellido());
        paciente.setUserRole(UserRole.PACIENTE);
        paciente.setFechaCreacion(new Date());
        paciente.setUser(usuario);
        paciente.setEstado(false);
        paciente.setPerfilCompletado(false);
        paciente = pacienteRepository.save(paciente);

        String enlace = "http://localhost:5173/register/" + paciente.getIdPaciente();
        emailService.enviarCorreoConEnlace(paciente.getEmail(), enlace, usuario, paciente );

        return paciente;
    }
}
