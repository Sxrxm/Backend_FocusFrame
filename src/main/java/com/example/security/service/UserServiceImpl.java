package com.example.security.service;

import com.example.model.Paciente;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.UserRepository;
import com.example.security.dto.AuthenticatedUserDto;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import com.example.security.exception.EntityNotFoundException;
import com.example.security.mapper.UserMapper;
import com.example.security.utils.ValidarEdad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserServiceImpl implements UserService {


	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	private static final String SPECIAL_CHARACTERS_PATTERN = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).*$";



	/*private static final String REGISTRATION_SUCCESSFUL = "registration_successful";*/

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserValidationService userValidationService;
    private final FileStorageService fileStorageService;



    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserValidationService userValidationService, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userValidationService = userValidationService;
        this.fileStorageService = fileStorageService;
    }




    @Override
	public User findByEmail(String Email) {
		return userRepository.findByEmail(Email);
	}

	@Override
	public RegistrationResponse registration(RegistrationRequest registrationRequest, Locale locale) {

		if (!validarContrasena(registrationRequest.getPassword())) {
			return new RegistrationResponse("La contraseña debe contener al menos un carácter especial.");
		}

		userValidationService.validateUser(registrationRequest);

        if (registrationRequest.getUserRole() == UserRole.ADMIN) {
            ValidarEdad.validarMayorDeEdad(registrationRequest.getFechaNacimiento(), "ADMIN");
        }



        final User user = UserMapper.INSTANCE.convertToUser(registrationRequest);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setFechaNacimiento(registrationRequest.getFechaNacimiento());
		user.setUserRole(UserRole.valueOf("ADMIN"));



		userRepository.save(user);

		final String email = registrationRequest.getEmail();
		log.info("Registro realizado con éxito.: {}", user.getEmail());
		/*String registrationSuccessMessage = messageSource.getMessage(
				REGISTRATION_SUCCESSFUL, new Object[]{email}, Locale.getDefault());*/


		return new RegistrationResponse("Registro realizado con éxito <3.");
	}

	private boolean validarContrasena(String password) {
		Pattern pattern = Pattern.compile(SPECIAL_CHARACTERS_PATTERN);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

    public User saveFotoPerfil(Long id, MultipartFile file) throws IOException {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));


        String fileName = fileStorageService.saveFile(file);
        usuario.setFotoPerfil(fileName);
        return userRepository.save(usuario);
    }


    public User updateFotoPerfil(Long id, MultipartFile file) throws IOException {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        System.out.println("validando user");
        String fotoAntigua = usuario.getFotoPerfil();
        System.out.println("recibiendo foto");
        String fotoNueva = fileStorageService.updateFile(file, fotoAntigua);
        System.out.println("se recibe");

        usuario.setFotoPerfil(fotoNueva);
        System.out.println("se actualiza");
        return userRepository.save(usuario);
    }

    public File getFotoPerfil(Long id) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        return fileStorageService.getFile(usuario.getFotoPerfil());
    }

    public User desactivarUsuario(Long idUsuario){
        Optional<User> usuarioExistente = userRepository.findById(idUsuario);

        if (usuarioExistente.isPresent()){
            User usuario = usuarioExistente.get();
            usuario.setEstado(false);
            usuario.setFechaDesactivacion(new Date());
            return userRepository.save(usuario);
        } else {
            throw new EntityNotFoundException("user.not.found");
        }
    }

	@Override
	public AuthenticatedUserDto findAuthenticatedUserByEmail(String email) {
		final User user = findByEmail(email);
		if (user == null) {
			return null;
		}
		return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
	}


}
