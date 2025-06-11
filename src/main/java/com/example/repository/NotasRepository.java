package com.example.repository;

import com.example.model.Funcionario;
import com.example.model.NotaSesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotasRepository extends JpaRepository<NotaSesion, Long> {
    List<NotaSesion> findByFuncionarioAndCategoriaNota(Funcionario funcionario, NotaSesion.CategoriaNota categoriaNota);

    List<NotaSesion> findByFuncionario(Funcionario funcionario);

}
