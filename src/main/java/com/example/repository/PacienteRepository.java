package com.example.repository;

import com.example.model.Paciente;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByDocumento(int documento);
    Paciente findByEmail(String email);
}
