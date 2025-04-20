package com.example.security.utils;

import java.time.LocalDate;
import java.time.Period;

public class ValidarEdad {
    public static void validarMayorDeEdad(LocalDate fechaNacimiento, String rol) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria ");
        }

        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 18) {
            throw new IllegalArgumentException("El usuario debe ser mayor de edad ");
        }
    }
}
