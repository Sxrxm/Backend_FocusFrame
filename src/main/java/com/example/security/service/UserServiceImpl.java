package com.example.security.service;

import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.UserRepository;
import com.example.security.dto.AuthenticatedUserDto;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import com.example.security.mapper.UserMapper;
import com.example.security.utils.ValidarEdad;
import com.example.service.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
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



    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userValidationService = userValidationService;
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

		userValidationService.validateUser(registrationRequest, locale);

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

	@Override
	public AuthenticatedUserDto findAuthenticatedUserByEmail(String email) {
		final User user = findByEmail(email);
		if (user == null) {
			return null;
		}
		return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
	}


}
