package com.example.dto;


import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;


public class CardPaciente {
    private Long id;
    private String nombrePaciente;
    private String email;
    private Long telefono;
    private boolean estado;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Bogota")
    private Date fechaCreacionHistorial;
    private int sesionesCompletadas;
    private int sesionesTotales;
    private String nombreTerapia;
    private double porcentajeTerapia;
    private boolean tieneCitasPendientes;


    public CardPaciente(Long id, String nombrePaciente, String email, Long telefono, boolean estado, Date fechaCreacionHistorial, int sesionesCompletadas, int sesionesTotales, String nombreTerapia, double porcentajeTerapia, boolean tieneCitasPendientes) {
        this.id = id;
        this.nombrePaciente = nombrePaciente;
        this.email = email;
        this.telefono = telefono;
        this.estado = estado;
        this.fechaCreacionHistorial = fechaCreacionHistorial;
        this.sesionesCompletadas = sesionesCompletadas;
        this.sesionesTotales = sesionesTotales;
        this.nombreTerapia = nombreTerapia;
        this.porcentajeTerapia = porcentajeTerapia;
        this.tieneCitasPendientes = tieneCitasPendientes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacionHistorial() {
        return fechaCreacionHistorial;
    }

    public void setFechaCreacionHistorial(Date fechaCreacionHistorial) {
        this.fechaCreacionHistorial = fechaCreacionHistorial;
    }

    public int getSesionesCompletadas() {
        return sesionesCompletadas;
    }

    public void setSesionesCompletadas(int sesionesCompletadas) {
        this.sesionesCompletadas = sesionesCompletadas;
    }

    public int getSesionesTotales() {
        return sesionesTotales;
    }

    public void setSesionesTotales(int sesionesTotales) {
        this.sesionesTotales = sesionesTotales;
    }

    public String getNombreTerapia() {
        return nombreTerapia;
    }

    public void setNombreTerapia(String nombreTerapia) {
        this.nombreTerapia = nombreTerapia;
    }

    public double getPorcentajeTerapia() {
        return porcentajeTerapia;
    }

    public void setPorcentajeTerapia(double porcentajeTerapia) {
        this.porcentajeTerapia = porcentajeTerapia;
    }

    public boolean isTieneCitasPendientes() {
        return tieneCitasPendientes;
    }

    public void setTieneCitasPendientes(boolean tieneCitasPendientes) {
        this.tieneCitasPendientes = tieneCitasPendientes;
    }
}
