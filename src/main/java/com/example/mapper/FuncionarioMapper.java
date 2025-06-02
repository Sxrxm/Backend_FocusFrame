package com.example.mapper;

import com.example.dto.FuncionarioPaso1Request;
import com.example.model.Funcionario;
import com.example.model.User;
import com.example.security.dto.RegistrationRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    @Mapping(target = "idFuncionario", ignore = true)
    @Mapping(target = "estado", constant = "false")
    @Mapping(target = "fechaCreacion", expression = "java(new java.util.Date())")
    @Mapping(target = "especialidad", constant = "null")
    @Mapping(target = "experiencia", constant = "null")
    @Mapping(target = "licencia", constant = "null")
    @Mapping(target = "user", ignore = true)
    Funcionario toFuncionarioFromPaso1(FuncionarioPaso1Request request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userRole", constant = "PSICOLOGO")
    @Mapping(target = "password", ignore = true)
    User toUserFromRegistrationRequest(RegistrationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFuncionarioPaso3(@MappingTarget Funcionario existing, Funcionario nuevosDatos);
}
