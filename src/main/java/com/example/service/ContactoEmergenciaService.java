package com.example.service;

import com.example.dto.HistorialClinicoDto;
import com.example.model.ContactoEmergencia;
import com.example.repository.ContactoEmergenciaRepository;
import com.example.security.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class ContactoEmergenciaService {

    private final ContactoEmergenciaRepository contactoEmergenciaRepository;

    @Autowired
    public ContactoEmergenciaService(ContactoEmergenciaRepository contactoEmergenciaRepository) {
        this.contactoEmergenciaRepository = contactoEmergenciaRepository;
    }

    @Transactional
    public ContactoEmergencia crearContactoEmergencia(@RequestBody HistorialClinicoDto.ContactoEmergenciaDto dto) {
        ContactoEmergencia contacto = new ContactoEmergencia();
        contacto.setNombre(dto.getNombre());
        contacto.setApellido(dto.getApellido());
        contacto.setParentesco(dto.getParentesco());
        contacto.setTelefono(dto.getTelefono());
        contacto.setCorreo(dto.getCorreo());
        return contactoEmergenciaRepository.save(contacto);
    }
    public Optional<ContactoEmergencia> obtenerContactoEmergencia(Long id) {
        return contactoEmergenciaRepository.findById(id);
    }

    @Transactional
    public ContactoEmergencia actualizarContactoEmergencia(Long id, HistorialClinicoDto.ContactoEmergenciaDto contactoEmergenciaDto) {
        ContactoEmergencia contactoEmergencia = contactoEmergenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        contactoEmergencia.setNombre(contactoEmergenciaDto.getNombre());
        contactoEmergencia.setApellido(contactoEmergenciaDto.getApellido());
        contactoEmergencia.setParentesco(contactoEmergenciaDto.getParentesco());
        contactoEmergencia.setCorreo(contactoEmergenciaDto.getCorreo());
        contactoEmergencia.setTelefono(contactoEmergenciaDto.getTelefono());

        return contactoEmergenciaRepository.save(contactoEmergencia);
    }

    public void eliminarContactoEmergencia(Long id) {
        contactoEmergenciaRepository.deleteById(id);
    }
}

