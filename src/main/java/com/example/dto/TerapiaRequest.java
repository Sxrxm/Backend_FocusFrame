package com.example.dto;

import com.example.model.Sesion;
import com.example.model.Terapia;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.util.Set;

public class TerapiaRequest {
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Set<Sesion> sesiones;
    private int numeroSesiones;

    @Enumerated(EnumType.STRING)
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



    public int getNumeroSesiones() {
        return numeroSesiones;
    }

    public void setNumeroSesiones(int numeroSesiones) {
        this.numeroSesiones = numeroSesiones;
    }

    public Set<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(Set<Sesion> sesiones) {
        this.sesiones = sesiones;
    }
}

