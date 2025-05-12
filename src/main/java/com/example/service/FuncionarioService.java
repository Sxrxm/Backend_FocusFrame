package com.example.service;


import com.example.model.Funcionario;
import com.example.model.Paciente;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.FuncionarioRepository;
import com.example.repository.UserRepository;
import com.example.security.dto.FuncionarioPaso1Request;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import com.example.security.utils.ValidarEdad;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final UserValidationService userValidationService;

    @Autowired
    public FuncionarioService(FuncionarioRepository funcionarioRepository, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, MessageSource messageSource, UserValidationService userValidationService) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.userValidationService = userValidationService;
    }


    @Transactional
    public Optional<Funcionario> getFuncionarioById (Long id){
        return funcionarioRepository.findById(id);
    }


    @Transactional
    public List<Funcionario> getAllFuncionario (){
        return funcionarioRepository.findAll();
    }


    @Transactional
    public Funcionario paso1(FuncionarioPaso1Request request, Locale locale) {

        if (request.getNombre() == null || request.getNombre().trim().isEmpty() ||
                request.getApellido() == null || request.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("nombre.apellido.required", null, locale));
        }


        User usuario = new User();
        usuario.setUserRole(UserRole.PSICOLOGO);


        Funcionario funcionario = new Funcionario();
        funcionario.setNombre(request.getNombre());
        funcionario.setApellido(request.getApellido());
        funcionario.setEspecialidad("null");
        funcionario.setExperiencia("null");
        funcionario.setLicencia("null");
        funcionario.setEstado(false);

        return funcionarioRepository.save(funcionario);

    }

    @Transactional
    public Map<String, Object> paso2(Long idFuncionario, RegistrationRequest registrationRequest, Locale locale) {

        userValidationService.validateUser(registrationRequest, locale);

        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("funcionario.not.found", null, locale)));


        String documento = String.valueOf(registrationRequest.getDocumento());

        if (funcionarioRepository.findByDocumento(registrationRequest.getDocumento()) != null){
            throw new IllegalArgumentException(messageSource.getMessage("doc.register", null, locale));
        }

        if (documento == null || !documento.matches("\\d{6,11}")) {
            throw new RuntimeException(messageSource.getMessage("doc.length.invalid", null, locale));
        }



        ValidarEdad.validarMayorDeEdad(registrationRequest.getFechaNacimiento(), "PSICOLOGO");


        User usuario = new User();
        usuario.setUserRole(UserRole.PSICOLOGO);
        usuario.setFechaNacimiento(registrationRequest.getFechaNacimiento());
        usuario.setTipoDoc(registrationRequest.getTipoDoc());
        usuario.setEmail(registrationRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        usuario.setUsername(registrationRequest.getUsername());

        userRepository.save(usuario);

        funcionario.setDocumento(registrationRequest.getDocumento());
        funcionario.setUser(usuario);
        funcionarioRepository.save(funcionario);

        Map<String, Object> response = new HashMap<>();
        response.put("idusuario", usuario.getId());
        return response;
    }


    @Transactional
    public Funcionario paso3(Long idUsuario, Funcionario datosNuevos, Locale locale) {

        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("user.not.found", null, locale)));

        Funcionario funcionarioExistente = funcionarioRepository.findByUserId(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("funcionario.not.found", null, locale)));

        funcionarioExistente.setLicencia(datosNuevos.getLicencia());
        funcionarioExistente.setExperiencia(datosNuevos.getExperiencia());
        funcionarioExistente.setEspecialidad(datosNuevos.getEspecialidad());
        funcionarioExistente.setEstado(true);

        return funcionarioRepository.save(funcionarioExistente);
    }


    @Transactional
    public String eliminarFuncionario(Long funcionadioId, Locale locale) {
        Funcionario funcionario = funcionarioRepository.findById(funcionadioId)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("funcionario.not.found", null,locale)));

        funcionarioRepository.delete(funcionario);
        User usuario = funcionario.getUser();
        if (usuario != null) {
            userRepository.delete(usuario);
        }
        return "Funcionario y usuario eliminados";
    }
}
