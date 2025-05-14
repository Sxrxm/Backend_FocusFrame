package com.example.repository;

import com.example.model.ContactoEmergencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ContactoEmergenciaRepository extends JpaRepository< ContactoEmergencia, Long> {
//    Optional<ContactoEmergencia> findByPacienteId(Long idPaciente);
}
