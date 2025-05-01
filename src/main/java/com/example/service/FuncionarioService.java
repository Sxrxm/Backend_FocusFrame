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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private FuncionarioRepository funcionarioRepository;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    public FuncionarioService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Optional<Funcionario> getFuncionarioById (Long id){
        return funcionarioRepository.findById(id);
    }

    public List<Funcionario> getAllFuncionario (){
        return funcionarioRepository.findAll();
    }

    public Funcionario paso1(FuncionarioPaso1Request request) {

        User usuario = new User();
        usuario.setUserRole(UserRole.PSICOLOGO);
        usuario.setEmail(null);
        usuario.setPassword(null);
        usuario.setUsername(null);
        usuario = userRepository.save(usuario);


        Funcionario funcionario = new Funcionario();
        funcionario.setNombre(request.getNombre());
        funcionario.setApellido(request.getApellido());
        funcionario.setEspecialidad("null");
        funcionario.setExperiencia("null");
        funcionario.setLicencia("null");
        funcionario.setEstado(false);
        funcionario.setUser(usuario);
        return funcionarioRepository.save(funcionario);

    }

    public RegistrationResponse paso2(Long idFuncionario, RegistrationRequest registrationRequest, Locale locale) {

        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("funcionario.register", null, locale)));

        if (userRepository.findByEmail(registrationRequest.getEmail()) != null) {
            throw new IllegalArgumentException(messageSource.getMessage("email.use", null, locale));
        }

        if (funcionarioRepository.findByDocumento(registrationRequest.getDocumento()) != null){
            throw new IllegalArgumentException(messageSource.getMessage("doc.register", null, locale));
        }

        if (userRepository.findByUsername(registrationRequest.getUsername()) != null) {
            throw new IllegalArgumentException(messageSource.getMessage("username.found",null,locale));
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

        return new RegistrationResponse("Usuario registrado exitosamente");
    }

    public Funcionario paso3(Long idUsuario, Funcionario datosNuevos, Locale locale) {

        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException(messageSource.getMessage("user.not.found", null, locale)));

        Funcionario funcionarioExistente = funcionarioRepository.findByUserId(idUsuario)
                .orElseThrow(() -> new RuntimeException("Funcionario no encontrado"));

        funcionarioExistente.setLicencia(datosNuevos.getLicencia());
        funcionarioExistente.setExperiencia(datosNuevos.getExperiencia());
        funcionarioExistente.setEspecialidad(datosNuevos.getEspecialidad());
        funcionarioExistente.setEstado(true);

        return funcionarioRepository.save(funcionarioExistente);
    }



    public String eliminarFuncionario(Long funcionadioId) {
        Funcionario funcionario = funcionarioRepository.findById(funcionadioId)
                .orElseThrow(() -> new IllegalArgumentException("funcionario no encontrado"));

        funcionarioRepository.delete(funcionario);
        User usuario = funcionario.getUser();
        if (usuario != null) {
            userRepository.delete(usuario);
        }
        return "Funcionario y usuario eliminados";
    }
}
