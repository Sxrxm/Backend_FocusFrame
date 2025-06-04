package com.example.dto;

import com.example.model.Paciente;
import com.example.model.TipoDoc;

import java.time.LocalDate;

public class PacienteResponse {
    private String nombrePaciente;
    private Long telefono;
    private LocalDate fechaNacimiento;
    private TipoDoc tipoDoc;
    private Integer documento;
    private Boolean estado;
    private String email;
    private String username;
    private int edad;
    private boolean menorEdad;

    public PacienteResponse() {
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public PacienteResponse(Paciente paciente) {
        this.nombrePaciente = paciente.getNombre() + " " + paciente.getApellido();
        this.telefono = paciente.getTelefono();
        this.fechaNacimiento = paciente.getFechaNacimiento();
        this.tipoDoc = paciente.getTipoDoc();
        this.documento = paciente.getDocumento();
        this.estado = paciente.getEstado();
        this.email = paciente.getEmail();
        this.edad = paciente.getEdad();;
    }

    public TipoDoc getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoDoc tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public int getEdad() { return edad; }

    public void setEdad(int edad) { this.edad = edad; }

    public boolean isMenorEdad() { return menorEdad; }

    public void setMenorEdad(boolean menorEdad) { this.menorEdad = menorEdad;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }


}
