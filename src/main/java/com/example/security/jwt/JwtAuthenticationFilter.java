package com.example.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.security.service.UserDetailsServiceImpl;
import com.example.security.utils.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtTokenManager jwtTokenManager;
	private final UserDetailsServiceImpl userDetailsService;

	public JwtAuthenticationFilter(JwtTokenManager jwtTokenManager, UserDetailsServiceImpl userDetailsService) {
		this.jwtTokenManager = jwtTokenManager;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		final String header = request.getHeader(SecurityConstants.HEADER_STRING);

		String email = null;
		String authToken = null;

		if (Objects.nonNull(header) && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			authToken = header.replace(SecurityConstants.TOKEN_PREFIX, Strings.EMPTY);

			try {
				email = jwtTokenManager.getEmailFromToken(authToken);
			} catch (Exception e) {
				log.error("Authentication Exception: {}", e.getMessage());
				filterChain.doFilter(request, response);
				return;
			}
		}

		if (Objects.isNull(email) || Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
			filterChain.doFilter(request, response);
			return;
		}

		UserDetails user = userDetailsService.loadUserByUsername(email);
		boolean validToken = jwtTokenManager.validateToken(authToken, user.getUsername());

		if (!validToken) {
			filterChain.doFilter(request, response);
			return;
		}

		DecodedJWT decodedJWT = jwtTokenManager.getDecodedJWT(authToken);

		List<String> roles = decodedJWT.getClaim("roles").asList(String.class);



		Collection<GrantedAuthority> authorities = roles.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(user, null, authorities);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		log.info("Autenticacion exitosa. Ingresaste con: {}", email);

		filterChain.doFilter(request, response);
	}
}
