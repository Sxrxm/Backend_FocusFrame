package com.example.security.dto;

import com.example.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {

	private String email;

	private String password;

	private UserRole userRole;

}
