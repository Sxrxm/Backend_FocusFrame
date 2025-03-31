package com.example.service;


import com.example.model.Funcionario;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.FuncionarioRepository;
import com.example.repository.UserRepository;
import com.example.security.dto.FuncionarioPaso1Request;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public RegistrationResponse paso2(Long idFuncionario, RegistrationRequest registrationRequest) {

        if (userRepository.findByEmail(registrationRequest.getEmail()) != null) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));


        User usuario = new User();
        usuario.setUserRole(UserRole.PSICOLOGO);
        usuario.setEmail(registrationRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        usuario.setUsername(registrationRequest.getUsername());

        userRepository.save(usuario);

        funcionario.setUser(usuario);
        funcionario.setEstado(true);
        funcionarioRepository.save(funcionario);

        return new RegistrationResponse("Usuario registrado exitosamente");
    }

    public Funcionario paso3(Long idUsuario, Funcionario funcionario) {

        Optional<User> usuario = userRepository.findById(idUsuario);

        if (usuario.isPresent()) {
            funcionario.setUser(usuario.get());
            funcionario.setLicencia(funcionario.getLicencia());
            funcionario.setExperiencia(funcionario.getExperiencia());
            funcionario.setEspecialidad(funcionario.getEspecialidad());
            return funcionarioRepository.save(funcionario);
        }else {
            throw new RuntimeException("Usuario no encontrado.");
        }
    }
}
