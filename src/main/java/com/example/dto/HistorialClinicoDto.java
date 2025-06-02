package com.example.dto;


import com.example.model.HistorialClinico;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class HistorialClinicoDto {

    private List<HistorialClinico.Hobbies> hobbies;
    private String otroHobbie;

    private List<HistorialClinico.Medicamentos> medicamentos;
    private String otroMedicamento;

    private List<HistorialClinico.Enfermedades> enfermedades;
    private String otraEnfermedad;

    private String ocupacion;
    private String observacionesGenerales;

    private ContactoEmergenciaDto contactoEmergencia;


    private List<TerapiaDto> terapias;

    public static class TerapiaDto {
        private Long id;
        private String descripcion;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private List<SesionDto> sesiones;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public LocalDate getFechaInicio() {
            return fechaInicio;
        }

        public void setFechaInicio(LocalDate fechaInicio) {
            this.fechaInicio = fechaInicio;
        }

        public LocalDate getFechaFin() {
            return fechaFin;
        }

        public void setFechaFin(LocalDate fechaFin) {
            this.fechaFin = fechaFin;
        }

        public List<SesionDto> getSesiones() {
            return sesiones;
        }

        public void setSesiones(List<SesionDto> sesiones) {
            this.sesiones = sesiones;
        }
    }

    public List<TerapiaDto> getTerapias() {
        return terapias;
    }

    public void setTerapias(List<TerapiaDto> terapias) {
        this.terapias = terapias;
    }

    public static class SesionDto {
        private Long id;
        private String nombre;
        private LocalDate fechaSesion;
        private LocalTime hora;
        private String estado;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public LocalDate getFechaSesion() {
            return fechaSesion;
        }

        public void setFechaSesion(LocalDate fechaSesion) {
            this.fechaSesion = fechaSesion;
        }

        public LocalTime getHora() {
            return hora;
        }

        public void setHora(LocalTime hora) {
            this.hora = hora;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }



    public List<HistorialClinico.Hobbies> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<HistorialClinico.Hobbies> hobbies) {
        this.hobbies = hobbies;
    }

    public String getOtroHobbie() {
        return otroHobbie;
    }

    public void setOtroHobbie(String otroHobbie) {
        this.otroHobbie = otroHobbie;
    }

    public List<HistorialClinico.Medicamentos> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<HistorialClinico.Medicamentos> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getOtroMedicamento() {
        return otroMedicamento;
    }

    public void setOtroMedicamento(String otroMedicamento) {
        this.otroMedicamento = otroMedicamento;
    }

    public List<HistorialClinico.Enfermedades> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(List<HistorialClinico.Enfermedades> enfermedades) {
        this.enfermedades = enfermedades;
    }

    public String getOtraEnfermedad() {
        return otraEnfermedad;
    }

    public void setOtraEnfermedad(String otraEnfermedad) {
        this.otraEnfermedad = otraEnfermedad;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getObservacionesGenerales() {
        return observacionesGenerales;
    }

    public void setObservacionesGenerales(String observacionesGenerales) {
        this.observacionesGenerales = observacionesGenerales;
    }

    public ContactoEmergenciaDto getContactoEmergencia() {
        return contactoEmergencia;
    }

    public void setContactoEmergencia(ContactoEmergenciaDto contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia;
    }

    public static class ContactoEmergenciaDto {
        private String nombre;
        private String apellido;
        private String parentesco;
        private String correo;
        private long telefono;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public String getParentesco() {
            return parentesco;
        }

        public void setParentesco(String parentesco) {
            this.parentesco = parentesco;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public long getTelefono() {
            return telefono;
        }

        public void setTelefono(long telefono) {
            this.telefono = telefono;
        }
    }
}
