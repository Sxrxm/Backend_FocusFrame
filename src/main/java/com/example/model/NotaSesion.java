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
    private categoriaNota categoria;


    private enum categoriaNota{
        importante,
        proceso,
        altoPeligro
    }
}
