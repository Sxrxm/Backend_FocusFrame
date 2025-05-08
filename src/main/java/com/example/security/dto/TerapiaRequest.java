package com.example.security.dto;

import com.example.model.Sesion;
import com.example.model.Terapia;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class TerapiaRequest {
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long idPaciente;
    private Set<Sesion> sesiones;
    private Terapia.TipoTerapia tipoTerapia;

    public Terapia.TipoTerapia getTipoTerapia() {
        return tipoTerapia;
    }

    public void setTipoTerapia(Terapia.TipoTerapia tipoTerapia) {
        this.tipoTerapia = tipoTerapia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }


    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Set<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(Set<Sesion> sesiones) {
        this.sesiones = sesiones;
    }
}

