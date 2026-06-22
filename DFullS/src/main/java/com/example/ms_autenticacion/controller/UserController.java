package com.example.ms_autenticacion.controller;

import com.example.ms_autenticacion.assembler.UserModelAssembler;
import com.example.ms_autenticacion.model.User;
import com.example.ms_autenticacion.security.jwt.JwtService;
import com.example.ms_autenticacion.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserModelAssembler assembler;

    public UserController(AuthenticationManager authManager,
                          JwtService jwtService,
                          UserService userService,
                          UserModelAssembler assembler) {
        this.authManager = authManager;
        this.jwtService  = jwtService;
        this.userService = userService;
        this.assembler   = assembler;
    }

    // ── POST /register ────────────────────────────────────────────────────────

    @PostMapping("/register")
    @Operation(
            summary = "Registrar usuario",
            description = "Crea un nuevo usuario en el sistema. El RUN puede enviarse con o sin formato (12.000.000-0 o 120000000)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "RUN o email ya registrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
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

            return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(newUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    // ── POST /login ───────────────────────────────────────────────────────────

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario con RUN y contraseña, retorna un JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, retorna token JWT"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "RUN o contraseña incorrectos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
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

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "NO AUTORIZADO"));

        } catch (BadCredentialsException e) {
            log.warn("Login fallido para RUN: {} - credenciales incorrectas", runInput);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "RUN o Contraseña Incorrecto"));
        }
    }

    // ── PUT /{run} ────────────────────────────────────────────────────────────

    @PutMapping("/{run}")
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente según su RUN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email en uso"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<?> update(@PathVariable String run,
                                    @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.update(run, userDetails);
            return ResponseEntity.ok(assembler.toModel(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ── DELETE /{run} ─────────────────────────────────────────────────────────

    @DeleteMapping("/{run}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema según su RUN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<?> delete(@PathVariable String run) {
        try {
            userService.delete(run);
            // CORRECCIÓN: el original devolvía 200 con body en un DELETE exitoso.
            // El estándar REST para eliminación exitosa es 204 NO CONTENT.
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ── GET /{run} ────────────────────────────────────────────────────────────

    @GetMapping("/{run}")
    @Operation(
            summary = "Buscar usuario por RUN",
            description = "Retorna un usuario específico según su RUN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<?> getByRun(@PathVariable String run) {
        return userService.findByRun(run)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── GET /nombre/{nombre} ──────────────────────────────────────────────────


    @GetMapping("/nombre/{nombre}")
    @Operation(
            summary = "Buscar usuario por nombre",
            description = "Retorna un usuario específico según su nombre."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<?> getByNombre(@PathVariable String nombre) {
        return userService.findByNombre(nombre)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}