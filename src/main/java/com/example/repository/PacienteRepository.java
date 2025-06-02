package com.example.repository;

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
    List<Paciente> findByFuncionario_IdFuncionario(long funcionarioId);


    @Query("SELECT p FROM Paciente p WHERE " +
            "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(p.apellido) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Paciente> buscarPacientes(@Param("busqueda") String busqueda);

}
