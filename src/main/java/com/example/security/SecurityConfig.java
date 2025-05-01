package com.example.security;

import com.example.security.jwt.JwtAuthenticationFilter;
import com.example.security.jwt.JwtTokenManager;
import com.example.security.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenManager jwtTokenManager;

    public SecurityConfig(@Lazy UserDetailsServiceImpl userDetailsService, JwtTokenManager jwtTokenManager) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/auth/recuperarContra").permitAll()
                        .requestMatchers("/auth/recuperar-contrasena").permitAll()
                        .requestMatchers("/auth/validate-reset-token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/cambioContra").authenticated()
                        .requestMatchers(HttpMethod.POST,"/funcionario/paso1").permitAll()
                        .requestMatchers(HttpMethod.POST,"/funcionario/paso2/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/funcionario/paso3/**").permitAll()
                        .requestMatchers( "/paciente/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/historialClinico/consultarHistorial/{pacienteId}").hasAuthority("PSICOLOGO")
                        .requestMatchers(HttpMethod.GET, "/historialClinico/consultarHistorial/{pacienteId}").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.POST,"/historialClinico/crearHistorial/{idPaciente}").hasAuthority("PSICOLOGO")
                        .requestMatchers("/sesion/createSesion").hasAuthority("PSICOLOGO")
                        .requestMatchers("/sesion/createSesion").hasRole("PACIENTE")
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenManager, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true);
    }
}