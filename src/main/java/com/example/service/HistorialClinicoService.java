package com.example.service;

import com.example.model.ContactoEmergencia;
import com.example.model.HistorialClinico;
import com.example.model.Paciente;
import com.example.repository.ContactoEmergenciaRepository;
import com.example.repository.HistorialClinicoRepository;
import com.example.repository.PacienteRepository;
import com.example.security.dto.HistorialClinicoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Service
public class HistorialClinicoService {

    private final HistorialClinicoRepository historialClinicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ContactoEmergenciaRepository contactoEmergenciaRepository;

    @Autowired
    private MessageSource messageSource;


    public HistorialClinicoService(HistorialClinicoRepository historialClinicoRepository, PacienteRepository pacienteRepository, ContactoEmergenciaRepository contactoEmergenciaRepository) {
        this.historialClinicoRepository = historialClinicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.contactoEmergenciaRepository = contactoEmergenciaRepository;
    }

    public HistorialClinico crearHistorial(Long pacienteId, HistorialClinicoDto historialClinicoDto, Locale locale) {
        Paciente paciente = pacienteRepository.findById(pacienteId).orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("user.not.found", null, locale)));

        if (historialClinicoRepository.existsByPaciente(paciente)) {

            throw new IllegalArgumentException(messageSource.getMessage("medical.history.found", null, locale));

        }

        HistorialClinico historialClinico = new HistorialClinico();
        historialClinico.setPaciente(paciente);
        historialClinico.setFechaCreacion(new Date());
        historialClinico.setUltimaActualizacion(LocalDateTime.now());


        historialClinico.setHobbies(historialClinicoDto.getHobbies());
        historialClinico.setOtroHobbie(historialClinicoDto.getOtroHobbie());
        historialClinico.setMedicamentos(historialClinicoDto.getMedicamentos());
        historialClinico.setOtroMedicamento(historialClinicoDto.getOtroMedicamento());
        historialClinico.setEnfermedades(historialClinicoDto.getEnfermedades());
        historialClinico.setOtraEnfermedad(historialClinicoDto.getOtraEnfermedad());
        historialClinico.setOcupacion(historialClinicoDto.getOcupacion());
        historialClinico.setObservacionesGenerales(historialClinicoDto.getObservacionesGenerales());

        if (historialClinicoDto.getContactoEmergencia() != null) {
            HistorialClinicoDto.ContactoEmergenciaDto contactoEmergenciaDto = historialClinicoDto.getContactoEmergencia();
            ContactoEmergencia contactoEmergencia = new ContactoEmergencia();
            contactoEmergencia.setNombre(contactoEmergenciaDto.getNombre());
            contactoEmergencia.setApellido(contactoEmergenciaDto.getApellido());
            contactoEmergencia.setParentesco(contactoEmergenciaDto.getParentesco());
            contactoEmergencia.setCorreo(contactoEmergenciaDto.getCorreo());
            contactoEmergencia.setTelefono(contactoEmergenciaDto.getTelefono());

            contactoEmergencia = contactoEmergenciaRepository.save(contactoEmergencia);

            historialClinico.setContactoEmergencia(contactoEmergencia);
        }
        return  historialClinicoRepository.save(historialClinico);

    }

    public HistorialClinicoDto getHistorialClinico(Long pacienteId, Locale locale) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException(messageSource.getMessage("user.not.found", null, locale)));


        HistorialClinico historialClinico = historialClinicoRepository.findByPacienteIdWithAllData(pacienteId)
                .orElseThrow(() -> new RuntimeException(messageSource.getMessage("medical.history.not.found", null, locale)));

        return convertToDto(historialClinico, paciente);
    }

    private HistorialClinicoDto convertToDto(HistorialClinico historialClinico, Paciente paciente) {
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

        return dto;
    }
    public HistorialClinico actualizarHistorialClinico(Long id, HistorialClinicoDto historialClinicoDto, Locale locale) {
        HistorialClinico historialClinico = historialClinicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(messageSource.getMessage("medical.history.not.found", null, locale)));

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

        historialClinico.setUltimaActualizacion(LocalDateTime.now());

        return historialClinicoRepository.save(historialClinico);
    }

}
