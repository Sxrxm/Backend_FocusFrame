package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "sesiones")
@NoArgsConstructor
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id_sesion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Paciente paciente;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terapia_id", nullable = false)
    private Terapia terapia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHistorialClinico")
    private HistorialClinico historialClinico;


    @Column(nullable = false, length = 100, name = "nombre")
    private String nombre;

    @Column(nullable = false, name = "fecha_sesion")
    private LocalDate fechaSesion;

    @Column(name = "fecha_registro", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @Column(nullable = false, name = "hora")
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoSesion estado;


    public enum EstadoSesion {
        PENDIENTE, CONFIRMADA, CANCELADA, FINALIZADA
    }


    @PrePersist
    public void asignarFechaRegistro() {

            this.fechaRegistro = new Date();
    }


    public Terapia getTerapia() {
        return terapia;
    }

    public void setTerapia(Terapia terapia) {
        this.terapia = terapia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(LocalDate fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }


    public EstadoSesion getEstado() {
        return estado;
    }

    public void setEstado(EstadoSesion estado) {
        this.estado = estado;
    }
}
