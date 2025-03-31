package com.example.security.dto;

import com.example.model.Sesion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class SesionResponse {
    private Long id;
    private String nombre;
    private String nombreP;
    private String nombreF;
    private Long idFuncionario;
    private Long idPaciente;
    private LocalDate fechaSesion;
    private LocalTime hora;
    private BigDecimal monto;
    private Sesion.MetodoPago metodoPago;
    private Sesion.EstadoSesion estado;

    public SesionResponse(Long id, String nombre, String nombreP, String nombreF, Long idFuncionario, Long idPaciente, LocalDate fechaSesion, LocalTime hora, BigDecimal monto, Sesion.MetodoPago metodoPago, Sesion.EstadoSesion estado) {
        this.id = id;
        this.nombre = nombre;
        this.nombreP = nombreP;
        this.nombreF = nombreF;
        this.idFuncionario = idFuncionario;
        this.idPaciente = idPaciente;
        this.fechaSesion = fechaSesion;
        this.hora = hora;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estado = estado;
    }

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public String getNombreF() {
        return nombreF;
    }

    public void setNombreF(String nombreF) {
        this.nombreF = nombreF;
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

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Sesion.MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(Sesion.MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Sesion.EstadoSesion getEstado() {
        return estado;
    }

    public void setEstado(Sesion.EstadoSesion estado) {
        this.estado = estado;
    }
}
