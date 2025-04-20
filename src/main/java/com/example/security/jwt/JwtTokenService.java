package com.example.security.jwt;

import com.example.model.User;
import com.example.security.dto.LoginRequest;
import com.example.security.dto.LoginResponse;
import com.example.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
public class JwtTokenService {
	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


	private final UserService userService;
	private final JwtTokenManager jwtTokenManager;
	private final AuthenticationManager authenticationManager;

    public JwtTokenService(UserService userService, JwtTokenManager jwtTokenManager, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenManager = jwtTokenManager;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse getLoginResponse(LoginRequest loginRequest) {

		final String email = loginRequest.getEmail();
		final String password = loginRequest.getPassword();

		final UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(email, password);

		authenticationManager.authenticate(authenticationToken);

		final User user = userService.findByEmail(email);


		final String token = jwtTokenManager.generateToken(user);

		log.info("{} has successfully logged in!", user.getEmail());

		return new LoginResponse(token, user.getUsername(), user.getUserRole());
	}
}
