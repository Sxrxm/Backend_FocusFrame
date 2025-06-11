package com.example.service;


import com.example.dto.FuncionarioPaso1Request;
import com.example.mapper.FuncionarioMapper;
import com.example.model.Funcionario;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.FuncionarioRepository;
import com.example.repository.UserRepository;
import com.example.security.dto.RegistrationRequest;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.EntityNotFoundException;
import com.example.security.service.UserServiceImpl;
import com.example.security.service.UserValidationService;
import com.example.security.utils.ValidarEdad;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final UserValidationService userValidationService;
    private final FuncionarioMapper funcionarioMapper;


    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public FuncionarioService(FuncionarioRepository funcionarioRepository, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, MessageSource messageSource, UserValidationService userValidationService, FuncionarioMapper funcionarioMapper) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.userValidationService = userValidationService;
        this.funcionarioMapper = funcionarioMapper;
    }


    @Transactional
    public Funcionario getFuncionarioById (Long id){
        return funcionarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));
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

        Funcionario funcionario = funcionarioMapper.toFuncionarioFromPaso1(request);

        return funcionarioRepository.save(funcionario);

    }

    @Transactional
    public Map<String, Object> paso2(Long idFuncionario, RegistrationRequest registrationRequest) {

        Integer documento = registrationRequest.getDocumento();

        if (documento == null || !documento.toString().matches("\\d{6,11}")) {
            throw new BadRequestException("doc.length.invalid");
        }
        userValidationService.validateUser(registrationRequest);


        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));


        if (funcionarioRepository.findByDocumento(registrationRequest.getDocumento()) != null) {
            throw new BadRequestException("doc.register");
        }


        ValidarEdad.validarMayorDeEdad(registrationRequest.getFechaNacimiento(), "PSICOLOGO");


        User usuario = funcionarioMapper.toUserFromRegistrationRequest(registrationRequest);
        usuario.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        userRepository.save(usuario);

        funcionario.setDocumento(registrationRequest.getDocumento());
        funcionario.setUser(usuario);
        funcionarioRepository.save(funcionario);

        Map<String, Object> response = new HashMap<>();
        response.put("idusuario", usuario.getId());
        log.info("Usuario creado correctamente con ID: {}", usuario.getId());

        return response;

    }


    @Transactional
    public Funcionario paso3(Long idUsuario, Funcionario datosNuevos, Locale locale) {

        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        Funcionario funcionarioExistente = funcionarioRepository.findByUserId(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));


        funcionarioExistente.setEspecialidad(datosNuevos.getEspecialidad());
        funcionarioExistente.setExperiencia(datosNuevos.getExperiencia());
        funcionarioExistente.setLicencia(datosNuevos.getLicencia());
        funcionarioExistente.setEstado(true);

        return funcionarioRepository.save(funcionarioExistente);
    }


    @Transactional
    public String eliminarFuncionario(Long funcionadioId, Locale locale) {
        Funcionario funcionario = funcionarioRepository.findById(funcionadioId)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));

        funcionarioRepository.delete(funcionario);
        User usuario = funcionario.getUser();
        if (usuario != null) {
            userRepository.delete(usuario);
        }
        return "Funcionario y usuario eliminados";
    }
}
