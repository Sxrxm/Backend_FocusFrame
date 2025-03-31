package com.example.security.dto;



import com.example.model.Sesion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class SesionRequest {

    private String nombre;
    private Long idFuncionario;
    private Long idPaciente;
    private String usernameF;
    private String usernameP;
    private LocalDate fechaSesion;
    private LocalTime hora;
    private BigDecimal monto;
    private Sesion.MetodoPago metodoPago;

    public String getUsernameF() {
        return usernameF;
    }

    public void setUsernameF(String usernameF) {
        this.usernameF = usernameF;
    }

    public String getUsernameP() {
        return usernameP;
    }

    public void setUsernameP(String usernameP) {
        this.usernameP = usernameP;
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
}
