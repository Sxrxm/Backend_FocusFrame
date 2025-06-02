package com.example.dto;



import java.time.LocalDate;
import java.time.LocalTime;

public class SesionRequest {

    private String nombre;
    private Long idTerapia;
    private Long idPaciente;
    private Long idPsicologo;
    private LocalDate fechaSesion;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String notasAdicionales;

    public String getNotasAdicionales() {
        return notasAdicionales;
    }

    public void setNotasAdicionales(String notasAdicionales) {
        this.notasAdicionales = notasAdicionales;
    }

    public boolean isHorarioValido() {
        return horaFin.isAfter(horaInicio);
    }

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

    public Long getIdPsicologo() {
        return idPsicologo;
    }

    public void setIdPsicologo(Long idPsicologo) {
        this.idPsicologo = idPsicologo;
    }


}
