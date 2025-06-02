package com.example.security.dto;

import jakarta.validation.constraints.NotBlank;

public class RecuperarContraRequest {

    @NotBlank(message = "{password.not.null}")
    private String nueva;

    @NotBlank(message = "{password.not.null}")
    private String confirmar;

    public String getNueva() {
        return nueva;
    }

    public void setNueva(String nueva) {
        this.nueva = nueva;
    }

    public String getConfirmar() {
        return confirmar;
    }

    public void setConfirmar(String confirmar) {
        this.confirmar = confirmar;
    }
}
