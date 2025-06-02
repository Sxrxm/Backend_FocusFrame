package com.example.dto;

import com.example.model.Paciente;

public class HistorialClinicoResponse {
    private HistorialClinicoDto historialClinico;
    private PacienteResponse paciente;


    public HistorialClinicoResponse(HistorialClinicoDto dto, Paciente paciente) {
        this.historialClinico = dto;
        this.paciente = new PacienteResponse(paciente);
    }
    public HistorialClinicoDto getHistorialClinico() {
        return historialClinico;
    }

    public void setHistorialClinico(HistorialClinicoDto historialClinico) {
        this.historialClinico = historialClinico;
    }

    public PacienteResponse getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteResponse paciente) {
        this.paciente = paciente;
    }
}
