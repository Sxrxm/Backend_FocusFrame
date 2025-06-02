package com.example.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "historial_clinico")
@Getter
@Setter
@AllArgsConstructor
public class HistorialClinico {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idHistorialClinico", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contacto_emergencia")
    private ContactoEmergencia contactoEmergencia;

    @Column(name = "fecha_creacion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Bogota")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;



    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ultima_actualizacion")
    private Date ultimaActualizacion;

    @OneToMany(mappedBy = "historialClinico", cascade = CascadeType.ALL)
    private Set<Terapia> terapias;



    @Enumerated(EnumType.STRING)
    @Column(name = "hobbie")
    private List<Hobbies> hobbies = new ArrayList<>();

    private String otroHobbie;


    @Enumerated(EnumType.STRING)
    @Column(name = "medicamento")
    private List<Medicamentos> medicamentos = new ArrayList<>();

    private String otroMedicamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "enfermedad")
    private List<Enfermedades> enfermedades = new ArrayList<>();

    private String otraEnfermedad;


    @Column(name = "ocupacion")
    private String ocupacion;


    @Column(length = 2000, name = "observaciones_generales")
    private String observacionesGenerales;

    public enum Hobbies {
        DEPORTE,
        LECTURA,
        VIDEOJUEGOS,
        MUSICA,
        COCINA,
        NINGUNO,
        OTRO
    }

    public enum Medicamentos {
        Metformina,
        Losartan,
        Salbutamol,
        Fluoxetina,
        Alprazolam,
        Ibuprofeno,
        Aspirina,
        Paracetamol,
        Amoxicilina,
        Ciprofloxacino,
        Omeprazol,
        Simvastatina,
        Lisinopril,
        Sertralina,
        Diazepam,
        Ninguna,
        Otro
    }

    public enum Enfermedades {
         Diabetes,
         Hipertension,
         Asma,
         Depresion,
         Ansiedad,
         Artritis,
         Migrana,
         Esquizofrenia,
         TDAH,
         Autismo,
         Epilepsia,
         Cancer,
         EnfermedadCardiaca,
         Obesidad,
         Alzheimer,
         ninguna,
         otro
    }


    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Set<Terapia> getTerapias() {
        return terapias;
    }

    public void setTerapias(Set<Terapia> terapias) {
        this.terapias = terapias;
    }

    public HistorialClinico() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public ContactoEmergencia getContactoEmergencia() {
        return contactoEmergencia;
    }

    public void setContactoEmergencia(ContactoEmergencia contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia;
    }


    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }


    public List<Hobbies> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Hobbies> hobbies) {
        this.hobbies = hobbies;
    }

    public String getOtroHobbie() {
        return otroHobbie;
    }

    public void setOtroHobbie(String otroHobbie) {
        this.otroHobbie = otroHobbie;
    }

    public List<Medicamentos> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamentos> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getOtroMedicamento() {
        return otroMedicamento;
    }

    public void setOtroMedicamento(String otroMedicamento) {
        this.otroMedicamento = otroMedicamento;
    }

    public List<Enfermedades> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(List<Enfermedades> enfermedades) {
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
}
