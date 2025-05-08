package com.example.security.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.dto.AuthenticatedUserDto;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import com.example.service.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

public interface UserService {



    User findByEmail(String Email);

    RegistrationResponse registration(RegistrationRequest registrationRequest, Locale locale);

    AuthenticatedUserDto findAuthenticatedUserByEmail(String email);


}