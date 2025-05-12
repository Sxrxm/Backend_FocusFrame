package com.example.security.dto;

import com.example.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter

public class RegistrationResponse {


    private String message;

    public RegistrationResponse( String message) {
        this.message = message;
    }

    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

