package com.example.security.utils;

import com.example.security.exception.BadRequestException;

import java.time.LocalDate;
import java.time.Period;

public class ValidarEdad {
    public static void validarMayorDeEdad(LocalDate fechaNacimiento, String rol) {
        if (fechaNacimiento == null) {
            throw new BadRequestException("fecha.obligatoria");
        }

        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 18) {
            throw new BadRequestException("not.mayor.edad");
        }
    }
}
