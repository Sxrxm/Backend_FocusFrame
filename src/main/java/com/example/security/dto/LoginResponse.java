package com.example.security.dto;

import com.example.model.UserRole;

public class LoginResponse {

	private String token;

	private String username;

    private UserRole rol;

    public UserRole getRol() {
        return rol;
    }

    public void setRol(UserRole rol) {
        this.rol = rol;
    }


    public LoginResponse(String token, String username, UserRole rol) {
        this.token = token;
        this.username = username;
        this.rol = rol;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}



	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
