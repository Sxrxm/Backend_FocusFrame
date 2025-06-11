package com.example.repository;

import com.example.dto.CardPaciente;
import com.example.model.Funcionario;
import com.example.model.Paciente;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByDocumento(int documento);
    Optional<Paciente> findByEmail(String email);
    Page<Paciente> findAllByFuncionario(Funcionario funcionario, Pageable pageable);


    @Query("""
    SELECT p FROM Paciente p
    WHERE p.funcionario = :funcionario
    AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
         OR LOWER(p.apellido) LIKE LOWER(CONCAT('%', :busqueda, '%'))
         OR LOWER(p.email) LIKE LOWER(CONCAT('%', :busqueda, '%')))
    """)
    Page<Paciente> buscarPacientes(@Param("busqueda") String busqueda,
                                   Funcionario funcionario,
                                   Pageable pageable);

}
