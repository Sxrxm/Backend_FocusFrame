package com.example.service;

import com.auth0.jwt.JWT;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.dto.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidationService {

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Autowired
	private UserRepository userRepository;

	public UserValidationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	public void validateUser(RegistrationRequest registrationRequest) {
		if (userRepository.findByEmail(registrationRequest.getEmail()) != null) {
			throw new IllegalArgumentException("El correo electrónico ya está registrado");
		}
		if (userRepository.findByUsername(registrationRequest.getUsername()) != null) {
			throw new IllegalArgumentException("Este nombre de usuario ya se encuentra en uso.");
		}

		if (registrationRequest.getPassword().length() < 12) {
			throw new IllegalArgumentException("La contraseña debe tener al menos 12 caracteres");
		}

		if (registrationRequest.getEmail().length() < 8) {
			throw new IllegalArgumentException("Ingrese un correo valido");
		}
		if (!isValidEmail(registrationRequest.getEmail())) {
			throw new IllegalArgumentException(("El correo electronico no es valido"));
		}
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}