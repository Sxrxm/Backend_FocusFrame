package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class FuncionarioPaso1Request {

    @NotBlank(message = "Los nombres no pueden estar vacíos")
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellido;

    public @NotBlank(message = "Los nombres no pueden estar vacíos") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "Los nombres no pueden estar vacíos") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "Los apellidos no pueden estar vacíos") String getApellido() {
        return apellido;
    }

    public void setApellido(@NotBlank(message = "Los apellidos no pueden estar vacíos") String apellido) {
        this.apellido = apellido;
    }
}
