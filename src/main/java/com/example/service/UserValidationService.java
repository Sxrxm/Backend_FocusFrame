package com.example.service;

import com.example.repository.UserRepository;
import com.example.security.dto.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


	public void validateUser(RegistrationRequest registrationRequest, Locale locale) {
		if (userRepository.findByEmail(registrationRequest.getEmail()) != null) {
            throw new IllegalArgumentException(messageSource.getMessage("email.use", null, locale));
		}
		if (userRepository.findByUsername(registrationRequest.getUsername()) != null) {
            throw new IllegalArgumentException(messageSource.getMessage("username.found",null,locale));
		}

		if (registrationRequest.getPassword().length() < 12) {
			throw new IllegalArgumentException(messageSource.getMessage("password.too.short", null, locale));
		}

		if (registrationRequest.getEmail().length() < 8) {
			throw new IllegalArgumentException(messageSource.getMessage("email.too.short", null, locale));
		}
		if (!isValidEmail(registrationRequest.getEmail())) {
			throw new IllegalArgumentException((messageSource.getMessage("email.invalid", null, locale)));
		}
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}