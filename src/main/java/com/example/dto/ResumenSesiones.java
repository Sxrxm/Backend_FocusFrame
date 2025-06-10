package com.example.dto;


import com.example.model.Sesion;
import lombok.Data;

@Data
public class ResumenSesiones {
    private Sesion.EstadoSesion estado;
    private Long cantidad;

    public ResumenSesiones(Sesion.EstadoSesion estado, Long cantidad) {
        this.estado = estado;
        this.cantidad = cantidad;
    }

    public Sesion.EstadoSesion getEstado() {
        return estado;
    }

    public void setEstado(Sesion.EstadoSesion estado) {
        this.estado = estado;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}