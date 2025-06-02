package com.example.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "funcionario")
@Getter
@Setter
@NoArgsConstructor
public class Funcionario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "idFuncionario")
    private Long idFuncionario;

    @Column(name = "nombres", nullable = false)
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    private String apellido;

    @Column(nullable = false, name = "documento")
    private int documento;

    @Column(nullable = false, name = "especialidad")
    private String especialidad;


    @Column(nullable = false, name = "experiencia")
    private String experiencia;

    @Column(nullable = false, name = "licencia")
    private String licencia;

    @Column(nullable = false, name = "estado")
    private Boolean estado;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_usuario")
    private User user;

    @OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY)
    private List<Paciente> pacientes;

    @Column(name = "fecha_creacion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Bogota")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;


//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "funcionario")
//    @JsonIgnore
//    private List<Sesion> sesions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "funcionario")
    @JsonIgnore
    private List<Terapia> terapia;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "funcionario")
    @JsonIgnore
    private List<NotaSesion> notaSesions;

    public List<Terapia> getTerapia() {
        return terapia;
    }

    public void setTerapia(List<Terapia> terapia) {
        this.terapia = terapia;
    }

    public int getDocumento() {
        return documento;
    }

    public List<NotaSesion> getNotaSesions() {
        return notaSesions;
    }

    public void setNotaSesions(List<NotaSesion> notaSesions) {
        this.notaSesions = notaSesions;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellidos) {
        this.apellido = apellidos;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    //    public List<Sesion> getSesions() {
//        return sesions;
//    }
//
//    public void setSesions(List<Sesion> sesions) {
//        this.sesions = sesions;
//    }
}
