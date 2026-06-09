package com.example.ms_autenticacion.repository;

import com.example.ms_autenticacion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNombre(String nombre);

}

