package com.example.repository;

import com.example.model.Paciente;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByDocumento(int documento);
    Optional<Paciente> findByEmail(String email);
    List<Paciente> findByFuncionario_IdFuncionario(long funcionarioId);

    Page<Paciente> findByNombreContainingIgnoreCaseAndEmailContainingIgnoreCase(String nombre, String email, Pageable pageable);

}
