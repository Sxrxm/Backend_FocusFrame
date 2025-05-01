package com.example.repository;

import com.example.model.User;
import com.example.model.UserRole;
import com.example.security.dto.AuthenticatedUserDto;
import com.example.security.dto.RegistrationRequest;
import com.example.security.dto.RegistrationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    Optional<User> findByResetToken(String resetToken);

}
