package com.example.repository;


import com.example.model.HistorialClinico;
import com.example.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {

    //@Query("SELECT hc FROM historialClinico hc LEFT JOIN FETCH hc.sesion WHERE hc.paciente.id = :pacienteId")
    //Optional<HistorialClinico> findByPacienteIdWithSesion(@Param("pacienteId") Long pacienteId);

    @Query("SELECT h FROM HistorialClinico h LEFT JOIN FETCH h.sesiones WHERE h.paciente.id = :pacienteId")
    Optional<HistorialClinico> findByPacienteIdWithAllData(@Param("pacienteId") Long pacienteId);

    boolean existsByPaciente(Paciente paciente);
}
