package com.example.dto;


import com.example.model.TipoDoc;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class RegistroPacienteRequest {

    @NotEmpty(message = "El teléfono es obligatorio.")
    private Long telefono;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    private LocalDate fechaNacimiento;

    @NotNull(message = "seleccione un tipo de documento")
    private TipoDoc tipoDoc;

    @NotNull(message = "El documento es obligatorio.")
    private Integer documento;

    @NotNull(message = "El email es obligatorio.")
    private String email;

    @NotNull(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El apellido es obligatorio")
    private String apellido;


    public @NotNull(message = "seleccione un tipo de documento") TipoDoc getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(@NotNull(message = "seleccione un tipo de documento") TipoDoc tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }
}
