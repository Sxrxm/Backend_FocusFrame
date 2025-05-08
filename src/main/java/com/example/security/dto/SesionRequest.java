package com.example.security.dto;



import com.example.model.Sesion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class SesionRequest {

    private String nombre;
    private Long idTerapia;
    private Long idPaciente;
    private Long idPsicologo;
    private LocalDate fechaSesion;
    private LocalTime hora;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Long getIdTerapia() {
        return idTerapia;
    }

    public void setIdTerapia(Long idTerapia) {
        this.idTerapia = idTerapia;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public LocalDate getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(LocalDate fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Long getIdPsicologo() {
        return idPsicologo;
    }

    public void setIdPsicologo(Long idPsicologo) {
        this.idPsicologo = idPsicologo;
    }


}
