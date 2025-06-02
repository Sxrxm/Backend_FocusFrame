package com.example.security.service;

import com.example.repository.UserRepository;
import com.example.security.dto.RegistrationRequest;
import com.example.security.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidationService {

	private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Autowired
	public UserValidationService(UserRepository userRepository, MessageSource messageSource) {
		this.userRepository = userRepository;
        this.messageSource = messageSource;
    }


	public void validateUser(RegistrationRequest registrationRequest) {
		if (userRepository.findByEmail(registrationRequest.getEmail()) != null) {
            throw new BadRequestException("email.use");
		}
		if (userRepository.findByUsername(registrationRequest.getUsername()) != null) {
            throw new BadRequestException("username.found");
		}

		if (registrationRequest.getPassword().length() < 12) {
			throw new BadRequestException("password.too.short");
		}

		if (registrationRequest.getEmail().length() < 8) {
			throw new BadRequestException("email.too.short");
		}
		if (!isValidEmail(registrationRequest.getEmail())) {
			throw new BadRequestException("email.invalid");
		}
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}