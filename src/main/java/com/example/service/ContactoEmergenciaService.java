package com.example.service;

import com.example.model.ContactoEmergencia;
import com.example.repository.ContactoEmergenciaRepository;
import com.example.security.dto.HistorialClinicoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactoEmergenciaService {

    private final ContactoEmergenciaRepository contactoEmergenciaRepository;

    @Autowired
    public ContactoEmergenciaService(ContactoEmergenciaRepository contactoEmergenciaRepository) {
        this.contactoEmergenciaRepository = contactoEmergenciaRepository;
    }

    public ContactoEmergencia crearContactoEmergencia(ContactoEmergencia contactoEmergencia) {
        return contactoEmergenciaRepository.save(contactoEmergencia);
    }

    public Optional<ContactoEmergencia> obtenerContactoEmergencia(Long id) {
        return contactoEmergenciaRepository.findById(id);
    }

    public ContactoEmergencia actualizarContactoEmergencia(Long id, HistorialClinicoDto.ContactoEmergenciaDto contactoEmergenciaDto) {
        ContactoEmergencia contactoEmergencia = contactoEmergenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto de emergencia no encontrado"));

        // Actualizar los campos del contacto de emergencia
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

