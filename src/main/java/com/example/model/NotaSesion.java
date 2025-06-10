package com.example.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "notas")
@Getter
@Setter
@NoArgsConstructor
public class NotaSesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notaSesion")
    private Long id;

    @Column
    private String contenido;

    @Column(name = "fecha_creacion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Bogota")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Funcionario funcionario;


    @Column(nullable = false, name = "categoria_nota")
    private CategoriaNota categoria;


    public enum CategoriaNota {
        GENERAL,
        DIAGNOSTICO,
        TRATAMIENTO
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public CategoriaNota getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaNota categoria) {
        this.categoria = categoria;
    }
}
