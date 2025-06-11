package com.example.service;


import com.example.model.Funcionario;
import com.example.model.NotaSesion;
import com.example.repository.FuncionarioRepository;
import com.example.repository.NotasRepository;
import com.example.security.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Service
public class NotaSesionService {

    private final NotasRepository notasRepository;
    private final FuncionarioRepository funcionarioRepository;

    public NotaSesionService(NotasRepository notasRepository, FuncionarioRepository funcionarioRepository) {
        this.notasRepository = notasRepository;
        this.funcionarioRepository = funcionarioRepository;
    }


    @Transactional
    public NotaSesion crearNota(NotaSesion nota) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));

        nota.setFuncionario(funcionario);
        if (nota.getFechaCreacion() == null) {
            nota.setFechaCreacion(new Date());
        }

        return notasRepository.save(nota);
    }

    @Transactional
    public ResponseEntity<List<NotaSesion>> obtenerNotasPorCategoria(NotaSesion.CategoriaNota categoria){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));



        List<NotaSesion> notas = notasRepository.findByFuncionarioAndCategoriaNota(funcionario, categoria);
        if (notas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notas);
    }

    @Transactional
    public ResponseEntity<List<NotaSesion>> obtenerNotasPorPsicologo(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Funcionario funcionario = funcionarioRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("funcionario.not.found"));

        List<NotaSesion> notas = notasRepository.findByFuncionario(funcionario);
        return ResponseEntity.ok(notas);
    }


    @Transactional
    public ResponseEntity<NotaSesion> obtenerNotaPorId(Long id) {
        NotaSesion nota = notasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("nota.not.found"));
        return ResponseEntity.ok(nota);
    }


    @Transactional
    public void eliminarNota(Long idNota){
        NotaSesion nota = notasRepository.findById(idNota)
                .orElseThrow(() -> new EntityNotFoundException("nota.not.found"));
        notasRepository.delete(nota);
    }

    @Transactional
    public ResponseEntity<NotaSesion> actualizarNota(Long id, NotaSesion nuevaNota) {
        NotaSesion nota = notasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("nota.not.found"));

        nota.setCategoriaNota(nuevaNota.getCategoriaNota());
        nota.setFechaActualizacion(new Date());

        NotaSesion actualizada = notasRepository.save(nota);
        return ResponseEntity.ok(actualizada);
    }

}
