package com.example.repository;

import com.example.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    List<Sesion> findByEstado(String estado);

    List<Sesion> findByFechaSesion(LocalDate fechaSesion);

    List<Sesion> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT s FROM Sesion s WHERE s.funcionario.id = :idFuncionario AND s.fechaSesion = :fechaSesion")
    List<Sesion> findByFuncionarioIdAndFechaSesion(@Param("idFuncionario") Long idFuncionario, @Param("fechaSesion") LocalDate fechaSesion);


}
