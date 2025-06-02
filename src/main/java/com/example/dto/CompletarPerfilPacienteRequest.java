package com.example.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CompletarPerfilPacienteRequest {
    @NotEmpty(message = "{registration_username_not_empty}")
    @Size(min = 3, max = 50, message = "{registration_username_size}")
    private String username;

    @NotEmpty(message = "{registration_password_not_empty}")
    @Size(min = 8, message = "{registration_password_size}")
    private String password;

    public @NotEmpty(message = "{registration_username_not_empty}") @Size(min = 3, max = 50, message = "{registration_username_size}") String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "{registration_username_not_empty}") @Size(min = 3, max = 50, message = "{registration_username_size}") String username) {
        this.username = username;
    }

    public @NotEmpty(message = "{registration_password_not_empty}") @Size(min = 8, message = "{registration_password_size}") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "{registration_password_not_empty}") @Size(min = 8, message = "{registration_password_size}") String profilePassword) {
        this.password = profilePassword;
    }


}
