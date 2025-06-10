package com.example.mapper;


import com.example.dto.SesionRequest;
import com.example.dto.HistorialClinicoDto.SesionDto;
import com.example.dto.SesionResponse;
import com.example.model.Sesion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SesionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "funcionario", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "terapia", ignore = true)
    Sesion toEntity(SesionRequest request);

    @Mapping(source = "funcionario", target = "nombreFuncionario", qualifiedByName = "nombreCompletoFuncionario")
    @Mapping(source = "paciente", target = "nombrePaciente", qualifiedByName = "nombreCompletoPaciente")
    @Mapping(source = "sesion.horaInicio", target = "horaInicio")
    @Mapping(source = "sesion.horaFin", target = "horaFin")
    @Mapping(source = "sesion.notasAdicionales", target = "notasAdicionales")
    SesionResponse toResponse(Sesion sesion);


    @Named("nombreCompletoFuncionario")
    default String mapNombreCompletoFuncionario(com.example.model.Funcionario funcionario) {
        if (funcionario == null) return null;
        return funcionario.getNombre() + " " + funcionario.getApellido();
    }

    @Named("nombreCompletoPaciente")
    default String mapNombreCompletoPaciente(com.example.model.Paciente paciente) {
        if (paciente == null) return null;
        return paciente.getNombre() + " " + paciente.getApellido();
    }
}

