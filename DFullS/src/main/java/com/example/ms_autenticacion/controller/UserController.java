package com.example.ms_autenticacion.controller;

import com.example.ms_autenticacion.model.User;
import com.example.ms_autenticacion.security.jwt.JwtService;
import com.example.ms_autenticacion.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private AuthenticationManager authManager;
    private JwtService jwtService;
    private UserService userService;

    public UserController(AuthenticationManager authManager, JwtService jwtService, UserService userService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user){
        try{
            String roleToAssign = (user.getRole() == null || user.getRole().isEmpty())
                    ? "USER"
                    : user.getRole();
            User newUser = userService.register(
                    user.getRun(),
                    user.getNombre(),
                    user.getApellido(),
                    user.getEmail(),
                    user.getPassword(),
                    roleToAssign
            );

            return ResponseEntity.status(HttpStatus.CONFLICT).body(newUser);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("Error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Map<String, String> credentials) {
        String runInput = credentials.get("run");
        log.info("Intento de login para RUN: {}", runInput);
        try {
            String password = credentials.get("password");


            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(runInput, password)
            );

            if (auth.isAuthenticated()) {
                User user = userService.findByRun(runInput)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                String token = jwtService.generateToken(user.getRun(), user.getRole());
                log.info("Login exitoso para RUN: {}", runInput);
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "run", user.getRun(),
                        "role", user.getRole()
                ));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error", "NO AUTORIZADO"));
        } catch (BadCredentialsException e) {
            log.warn("Login fallido para RUN: {} - credenciales incorrectas", runInput);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error", "RUN o Contraseña Incorrecto"));
        }
    }

    //CRUD

    @PutMapping("/{run}")
    public ResponseEntity<?> update(@PathVariable String run,@Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.update(run, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{run}")
    public ResponseEntity<?> delete(@PathVariable String run) {
        try {
            userService.delete(run);
            return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }    @GetMapping("/{run}")
    public ResponseEntity<?> getByRun(@PathVariable String run) {
        return userService.findByRun(run)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{nombre}")
    public ResponseEntity<?> getdByName(@RequestBody String nombre) {
        return userService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



}
