package com.example.repository;

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

    List<Sesion> findByFuncionarioAndFechaSesionAndHora(Funcionario funcionario, LocalDate fechaSesion, LocalTime hora);

    List<Sesion> findByEstado(String estado);

    List<Sesion> findByFuncionarioAndFechaSesion(Funcionario funcionario,LocalDate fechaSesion);

    int countByFuncionarioIdFuncionarioAndFechaSesionAndHora(Long funcionarioId, LocalDate fechaSesion, LocalTime hora);

}
