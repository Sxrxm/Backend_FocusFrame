package com.example.repository;

import com.example.model.Funcionario;
import com.example.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findById(Long id);
    Optional<Funcionario> findByUserId(Long userId);
    Optional<Funcionario> findByUserEmail(String email);
    Funcionario findByDocumento(int documento);
}
