package com.example.repository;

import com.example.model.Paciente;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByDocumento(int documento);
    Optional<Paciente> findByEmail(String email);
}
