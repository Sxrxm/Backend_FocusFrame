package com.example.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "Terapia")
public class Terapia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTerapia")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_funcionario")
    @JsonIgnore
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPaciente")
    @JsonIgnore
    private Paciente paciente;

    @OneToMany(mappedBy = "terapia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Sesion> sesiones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHistorialClinico")
    @JsonIgnore
    private HistorialClinico historialClinico;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column
    private LocalDate fechaFin;


    @Enumerated(EnumType.STRING)
    @Column(name = "Tipo_terapia")
    private TipoTerapia TipoTerapia;

    public enum TipoTerapia {
        familiar,
        grupal,
        individual,
        pareja,
        infantil
    }

    public Terapia.TipoTerapia getTipoTerapia() {
        return TipoTerapia;
    }

    public void setTipoTerapia(Terapia.TipoTerapia tipoTerapia) {
        TipoTerapia = tipoTerapia;
    }

    public Terapia() {
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

    public Set<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(Set<Sesion> sesiones) {
        this.sesiones = sesiones;
    }

    public HistorialClinico getHistorialClinico() {
        return historialClinico;
    }

    public void setHistorialClinico(HistorialClinico historialClinico) {
        this.historialClinico = historialClinico;
    }
}
