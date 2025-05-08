package com.example.repository;


import com.example.model.HistorialClinico;
import com.example.model.Paciente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {


    @EntityGraph(attributePaths = {"terapias", "terapias.sesiones"})
    Optional<HistorialClinico> findByPacienteIdPaciente(Long idPaciente);



    boolean existsByPaciente(Paciente paciente);
}
