package com.example.dto;

import com.example.model.Sesion;

import java.time.LocalDate;
import java.time.LocalTime;

public class SesionResponse {
    private Long id;
    private String nombre;
    private String nombreFuncionario;
    private String nombrePaciente;
    private LocalDate fechaSesion;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Sesion.EstadoSesion estado;
    private String notasAdicionales;


    public SesionResponse(Long id, String nombre, String nombreFuncionario, String nombrePaciente, LocalDate fechaSesion, LocalTime horaInicio, LocalTime horaFin, Sesion.EstadoSesion estado, String notasAdicionales) {
        this.id = id;
        this.nombre = nombre;
        this.nombreFuncionario = nombreFuncionario;
        this.nombrePaciente = nombrePaciente;
        this.fechaSesion = fechaSesion;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.notasAdicionales = notasAdicionales;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreFuncionario() {
        return nombreFuncionario;
    }

    public void setNombreFuncionario(String nombreFuncionario) {
        this.nombreFuncionario = nombreFuncionario;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public LocalDate getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(LocalDate fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Sesion.EstadoSesion getEstado() {
        return estado;
    }

    public void setEstado(Sesion.EstadoSesion estado) {
        this.estado = estado;
    }

    public String getNotasAdicionales() {
        return notasAdicionales;
    }

    public void setNotasAdicionales(String notasAdicionales) {
        this.notasAdicionales = notasAdicionales;
    }
}