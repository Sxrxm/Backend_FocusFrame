package com.example.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.model.User;
import com.example.model.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenManager {

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Value("${jwt.expirationMinute}")
	private long expirationMinute;

	@Value("${jwt.issuer}")
	private String issuer;

	public String generateToken(User user) {
		String email = user.getEmail();

		List<String> roles = Arrays.asList(user.getUserRole().name());

		return JWT.create()
				.withSubject(email)
				.withIssuer(issuer)
				.withClaim("roles", roles)
				.withIssuedAt(user.getFechaCreacion())
				.withExpiresAt(new Date(System.currentTimeMillis() + expirationMinute * 60 * 1000))
				.sign(Algorithm.HMAC256(secretKey.getBytes()));
	}

	public String getEmailFromToken(String token) {
		DecodedJWT decodedJWT = getDecodedJWT(token);
		return decodedJWT.getSubject();
	}

	public boolean validateToken(String token, String authenticatedEmail) {
		String emailFromToken = getEmailFromToken(token);
		boolean equalsEmail = emailFromToken.equals(authenticatedEmail);
		boolean tokenExpired = isTokenExpired(token);
		return equalsEmail && !tokenExpired;
	}

	private boolean isTokenExpired(String token) {
		Date expirationDateFromToken = getExpirationDateFromToken(token);
		return expirationDateFromToken.before(new Date());
	}

	private Date getExpirationDateFromToken(String token) {
		DecodedJWT decodedJWT = getDecodedJWT(token);
		return decodedJWT.getExpiresAt();
	}

	public DecodedJWT getDecodedJWT(String token) {
		JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey.getBytes())).build();
		return jwtVerifier.verify(token);
	}
}
