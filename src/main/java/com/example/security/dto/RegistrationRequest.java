package com.example.security.dto;

import com.example.model.TipoDoc;
import com.example.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class RegistrationRequest {

	@NotEmpty(message = "{registration_username_not_empty}")
	private String username;

	@Email(message = "{registration_email_is_not_valid}")
	@NotEmpty(message = "{registration_email_not_empty}")
	private String email;



	@NotEmpty(message = "{registration_password_not_empty}")
	private String password;

    @NotNull(message = "seleccione un tipo de documento")
    private TipoDoc tipoDoc;

    @NotNull(message = "El documento es obligatorio.")
    private Integer documento;

	private UserRole userRole;


    private LocalDate fechaNacimiento;

    public @NotNull(message = "seleccione un tipo de documento") TipoDoc getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(@NotNull(message = "seleccione un tipo de documento") TipoDoc tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public @NotNull(message = "El documento es obligatorio.") Integer getDocumento() {
        return documento;
    }

    public void setDocumento(@NotNull(message = "El documento es obligatorio.") Integer documento) {
        this.documento = documento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
}