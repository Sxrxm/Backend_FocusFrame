package com.example.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "funcionario")
@Getter
@Setter
@NoArgsConstructor
public class Funcionario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id_funcionario")
    private Long idFuncionario;

    @Column( name = "nombres")
    private String nombre;

    @Column( name = "apellidos")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "funcionario")
    @JsonIgnore
    private List<Sesion> sesions;


    public int getDocumento() {
        return documento;
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

    public List<Sesion> getSesions() {
        return sesions;
    }

    public void setSesions(List<Sesion> sesions) {
        this.sesions = sesions;
    }
}
