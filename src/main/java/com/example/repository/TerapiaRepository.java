package com.example.repository;

import com.example.model.HistorialClinico;
import com.example.model.Terapia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TerapiaRepository extends JpaRepository<Terapia, Long> {

    List<Terapia> findByHistorialClinico(HistorialClinico historialClinico);
}
