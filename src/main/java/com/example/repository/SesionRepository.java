package com.example.repository;

import com.example.dto.ResumenSesiones;
import com.example.model.Funcionario;
import com.example.model.Paciente;
import com.example.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {

    List<Sesion> findByPacienteEmail(String email);


    List<Sesion> findByFuncionarioAndFechaSesion(Funcionario funcionario,LocalDate fechaSesion);

    List<Sesion> findByFuncionarioIdFuncionarioAndFechaSesion(Long idFuncionario, LocalDate fecha);

    @Query("SELECT new com.example.dto.ResumenSesiones(s.estado, COUNT(s)) " +
            "FROM Sesion s WHERE s.funcionario = :funcionario GROUP BY s.estado")
    List<ResumenSesiones> resumenSesiones(@Param("funcionario") Funcionario funcionario);


}
