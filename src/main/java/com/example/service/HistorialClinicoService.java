package com.example.service;

import com.example.dto.HistorialClinicoDto;
import com.example.dto.HistorialClinicoResponse;
import com.example.model.*;
import com.example.repository.ContactoEmergenciaRepository;
import com.example.repository.HistorialClinicoRepository;
import com.example.repository.PacienteRepository;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Service
public class HistorialClinicoService {

    private final HistorialClinicoRepository historialClinicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ContactoEmergenciaRepository contactoEmergenciaRepository;
    private final ContactoEmergenciaService contactoEmergenciaService;
    private final MessageSource messageSource;


    @Autowired
    public HistorialClinicoService(HistorialClinicoRepository historialClinicoRepository, PacienteRepository pacienteRepository, ContactoEmergenciaRepository contactoEmergenciaRepository, ContactoEmergenciaService contactoEmergenciaService, MessageSource messageSource) {
        this.historialClinicoRepository = historialClinicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.contactoEmergenciaRepository = contactoEmergenciaRepository;
        this.contactoEmergenciaService = contactoEmergenciaService;
        this.messageSource = messageSource;
    }


    @Transactional
    public HistorialClinico crearHistorial(Long pacienteId, HistorialClinicoDto historialClinicoDto) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("patient.not.found"));


        if (historialClinicoRepository.existsByPaciente(paciente)) {
            throw new EntityNotFoundException("medical.history.found");
        }

        HistorialClinico historialClinico = new HistorialClinico();
        historialClinico.setPaciente(paciente);
        historialClinico.setFechaCreacion(new Date());
        historialClinico.setUltimaActualizacion(new Date());


        historialClinico.setHobbies(historialClinicoDto.getHobbies());
        if (historialClinicoDto.getHobbies() != null && historialClinicoDto.getHobbies().contains(HistorialClinico.Hobbies.OTRO)) {
            if (historialClinicoDto.getOtroHobbie() == null || historialClinicoDto.getOtroHobbie().trim().isEmpty()) {
                throw new BadRequestException("hobby.other.required");
            }
        }
        historialClinico.setOtroHobbie(historialClinicoDto.getOtroHobbie());
        historialClinico.setMedicamentos(historialClinicoDto.getMedicamentos());
        if (historialClinicoDto.getMedicamentos() != null && historialClinicoDto.getMedicamentos().contains(HistorialClinico.Medicamentos.Otro)) {
            if (historialClinicoDto.getOtroMedicamento() == null || historialClinicoDto.getOtroMedicamento().trim().isEmpty()) {
                throw new BadRequestException("medicine.other.required");
            }
        }
        historialClinico.setOtroMedicamento(historialClinicoDto.getOtroMedicamento());
        historialClinico.setEnfermedades(historialClinicoDto.getEnfermedades());

        if (historialClinicoDto.getEnfermedades() != null && historialClinicoDto.getEnfermedades().contains(HistorialClinico.Enfermedades.otro)){
            if (historialClinicoDto.getOtraEnfermedad() == null || historialClinicoDto.getOtraEnfermedad().trim().isEmpty()) {
                throw new BadRequestException("disease.other.required");
            }
        }
        historialClinico.setOtraEnfermedad(historialClinicoDto.getOtraEnfermedad());
        historialClinico.setOcupacion(historialClinicoDto.getOcupacion());
        historialClinico.setObservacionesGenerales(historialClinicoDto.getObservacionesGenerales());

        if(historialClinicoDto.getContactoEmergencia() != null) {
            ContactoEmergencia contacto = contactoEmergenciaService.crearContactoEmergencia(historialClinicoDto.getContactoEmergencia());
            historialClinico.setContactoEmergencia(contacto);
        }

        return  historialClinicoRepository.save(historialClinico);

    }

    @Transactional
    public HistorialClinicoResponse getHistorialClinico(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        HistorialClinico historialClinico = historialClinicoRepository.findByPacienteIdPaciente(paciente.getIdPaciente())
                .orElseThrow(() -> new EntityNotFoundException("medical.history.not.found"));

        return convertToDto(historialClinico, paciente);
    }

    private HistorialClinicoResponse convertToDto(HistorialClinico historialClinico, Paciente paciente) {
        HistorialClinicoDto dto = new HistorialClinicoDto();

        dto.setHobbies(historialClinico.getHobbies());
        dto.setOtroHobbie(historialClinico.getOtroHobbie());
        dto.setMedicamentos(historialClinico.getMedicamentos());
        dto.setOtroMedicamento(historialClinico.getOtroMedicamento());
        dto.setEnfermedades(historialClinico.getEnfermedades());
        dto.setOtraEnfermedad(historialClinico.getOtraEnfermedad());
        dto.setOcupacion(historialClinico.getOcupacion());
        dto.setObservacionesGenerales(historialClinico.getObservacionesGenerales());

        if (historialClinico.getContactoEmergencia() != null) {
            HistorialClinicoDto.ContactoEmergenciaDto contactoEmergenciaDto = new HistorialClinicoDto.ContactoEmergenciaDto();
            contactoEmergenciaDto.setNombre(historialClinico.getContactoEmergencia().getNombre());
            contactoEmergenciaDto.setApellido(historialClinico.getContactoEmergencia().getApellido());
            contactoEmergenciaDto.setParentesco(historialClinico.getContactoEmergencia().getParentesco());
            contactoEmergenciaDto.setCorreo(historialClinico.getContactoEmergencia().getCorreo());
            contactoEmergenciaDto.setTelefono(historialClinico.getContactoEmergencia().getTelefono());
            dto.setContactoEmergencia(contactoEmergenciaDto);
        }

        List<HistorialClinicoDto.TerapiaDto> terapiaDtos = new ArrayList<>();
        for (Terapia terapia : historialClinico.getTerapias()) {
            HistorialClinicoDto.TerapiaDto terapiaDto = new HistorialClinicoDto.TerapiaDto();
            terapiaDto.setId(terapia.getId());
            terapiaDto.setDescripcion(terapia.getDescripcion());
            terapiaDto.setFechaInicio(terapia.getFechaInicio());
            terapiaDto.setFechaFin(terapia.getFechaFin());

            List<HistorialClinicoDto.SesionDto> sesionDtos = new ArrayList<>();
            for (Sesion sesion : terapia.getSesiones()) {
                HistorialClinicoDto.SesionDto sesionDto = new HistorialClinicoDto.SesionDto();
                sesionDto.setId(sesion.getId());
                sesionDto.setNombre(sesion.getNombre());
                sesionDto.setFechaSesion(sesion.getFechaSesion());
                sesionDto.setHora(sesion.getHoraInicio());
                sesionDto.setEstado(sesion.getEstado().name());
                sesionDtos.add(sesionDto);
            }

            terapiaDto.setSesiones(sesionDtos);
            terapiaDtos.add(terapiaDto);
        }

        dto.setTerapias(terapiaDtos);

        return new HistorialClinicoResponse(dto, paciente);
    }


    @Transactional
    public HistorialClinico actualizarHistorialClinico(Long id, HistorialClinicoDto historialClinicoDto) {
        HistorialClinico historialClinico = historialClinicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("medical.history.not.found"));

        historialClinico.setHobbies(historialClinicoDto.getHobbies());
        historialClinico.setMedicamentos(historialClinicoDto.getMedicamentos());
        historialClinico.setEnfermedades(historialClinicoDto.getEnfermedades());
        historialClinico.setOcupacion(historialClinicoDto.getOcupacion());
        historialClinico.setObservacionesGenerales(historialClinicoDto.getObservacionesGenerales());

        if (historialClinicoDto.getContactoEmergencia() != null) {
            HistorialClinicoDto.ContactoEmergenciaDto contactoEmergenciaDto = historialClinicoDto.getContactoEmergencia();
            if (historialClinico.getContactoEmergencia() == null) {

                ContactoEmergencia contactoEmergencia = new ContactoEmergencia();
                contactoEmergencia.setNombre(contactoEmergenciaDto.getNombre());
                contactoEmergencia.setApellido(contactoEmergenciaDto.getApellido());
                contactoEmergencia.setParentesco(contactoEmergenciaDto.getParentesco());
                contactoEmergencia.setCorreo(contactoEmergenciaDto.getCorreo());
                contactoEmergencia.setTelefono(contactoEmergenciaDto.getTelefono());

                contactoEmergencia = contactoEmergenciaRepository.save(contactoEmergencia);

                historialClinico.setContactoEmergencia(contactoEmergencia);
            } else {
                historialClinico.getContactoEmergencia().setNombre(contactoEmergenciaDto.getNombre());
                historialClinico.getContactoEmergencia().setApellido(contactoEmergenciaDto.getApellido());
                historialClinico.getContactoEmergencia().setParentesco(contactoEmergenciaDto.getParentesco());
                historialClinico.getContactoEmergencia().setCorreo(contactoEmergenciaDto.getCorreo());
                historialClinico.getContactoEmergencia().setTelefono(contactoEmergenciaDto.getTelefono());

                contactoEmergenciaRepository.save(historialClinico.getContactoEmergencia());
            }
        }

        historialClinico.setUltimaActualizacion(new Date());

        return historialClinicoRepository.save(historialClinico);
    }

}
