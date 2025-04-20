package com.example.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CambioContraRequest {


    private String actual;
    private String confirmar;
    private String nueva;

    @NotBlank(message = "{invalid.email}")
    @Email(message = "{invalid.email}")
    private String email;

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getConfirmar() {
        return confirmar;
    }

    public void setConfirmar(String confirmar) {
        this.confirmar = confirmar;
    }

    public String getNueva() {
        return nueva;
    }

    public void setNueva(String nueva) {
        this.nueva = nueva;
    }

    public @NotBlank(message = "{invalid.email}") @Email(message = "{invalid.email}") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "{invalid.email}") @Email(message = "{invalid.email}") String email) {
        this.email = email;
    }
}
