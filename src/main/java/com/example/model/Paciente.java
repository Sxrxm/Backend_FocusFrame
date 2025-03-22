package com.example.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "paciente")
@Getter
@Setter
@NoArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id_paciente")
    private Long idPaciente;

    @Column(nullable = false, name = "nombre")
    private String nombre;

    @Column(nullable = false, name = "apellido")
    private String apellido;


    @Column(nullable = false, name = "telefono")
    private long telefono;

    @Column(nullable = false, name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(nullable = false, name = "documento")
    private int documento;


    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean estado = true;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_usuario")
    private User user;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paciente")
    @JsonIgnore
    private List<Sesion> sesions;

    @Column(name = "fecha_creacion", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private UserRole userRole;

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

    public List<Sesion> getSesions() {
        return sesions;
    }

    public void setSesions(List<Sesion> sesions) {
        this.sesions = sesions;
    }

    @PrePersist
    public void asignarFechaCreacion() {
        this.fechaCreacion = new Date();
    }



    public boolean isPerfilCompletado() {
        return perfilCompletado;
    }

    public void setPerfilCompletado(boolean perfilCompletado) {
        this.perfilCompletado = perfilCompletado;
    }

    private boolean perfilCompletado = false;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }



    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
