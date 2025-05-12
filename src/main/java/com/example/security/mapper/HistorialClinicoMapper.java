package com.example.security.mapper;


import com.example.model.HistorialClinico;
import com.example.model.Paciente;
import com.example.security.dto.HistorialClinicoDto;
import com.example.security.dto.HistorialClinicoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistorialClinicoMapper {

    @Mapping(target = "dto" , source = "historialClinico")
    @Mapping(target = "paciente", source = "paciente")
    HistorialClinicoResponse toResponse(HistorialClinico historialClinico, Paciente paciente);

    HistorialClinicoDto toDto(HistorialClinico historialClinico);
}
