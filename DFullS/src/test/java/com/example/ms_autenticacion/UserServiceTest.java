package com.example.ms_autenticacion;

import com.example.ms_autenticacion.model.User;
import com.example.ms_autenticacion.repository.UserRepository;
import com.example.ms_autenticacion.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // ── helpers ───────────────────────────────────────────────────────────────


    private static final String RUN_LIMPIO  = "120000000";

    private static final String RUN_SUCIO   = "12.000.000-0";

    private User usuarioEjemplo() {
        return User.builder()
                .run(RUN_LIMPIO)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@example.com")
                .password("hashedPassword")
                .role("USER")
                .build();
    }

    // ── register ──────────────────────────────────────────────────────────────

    @Test
    void deberiaRegistrarUsuarioCuandoDatosValidos() {
        Mockito.when(userRepository.existsById(RUN_LIMPIO)).thenReturn(false);
        Mockito.when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(usuarioEjemplo());

        User resultado = userService.register(
                RUN_SUCIO, "Juan", "Pérez", "juan@example.com", "password123", "USER"
        );

        assertNotNull(resultado);
        assertEquals(RUN_LIMPIO, resultado.getRun());
        assertEquals("Juan", resultado.getNombre());

        verify(userRepository).existsById(RUN_LIMPIO);
        verify(userRepository).findByEmail("juan@example.com");
        verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void deberiaLanzarExcepcionAlRegistrarRunDuplicado() {
        Mockito.when(userRepository.existsById(RUN_LIMPIO)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.register(RUN_SUCIO, "Juan", "Pérez", "juan@example.com", "pass", "USER")
        );

        assertTrue(ex.getMessage().contains(RUN_LIMPIO));
        verify(userRepository).existsById(RUN_LIMPIO);
    }

    @Test
    void deberiaLanzarExcepcionAlRegistrarEmailDuplicado() {
        Mockito.when(userRepository.existsById(RUN_LIMPIO)).thenReturn(false);
        Mockito.when(userRepository.findByEmail("juan@example.com"))
                .thenReturn(Optional.of(usuarioEjemplo()));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.register(RUN_SUCIO, "Juan", "Pérez", "juan@example.com", "pass", "USER")
        );

        assertTrue(ex.getMessage().contains("juan@example.com"));
        verify(userRepository).existsById(RUN_LIMPIO);
        verify(userRepository).findByEmail("juan@example.com");
    }

    // ── registerSimple ────────────────────────────────────────────────────────

    @Test
    void deberiaRegistrarUsuarioSimpleConRolUser() {
        Mockito.when(userRepository.existsById(RUN_LIMPIO)).thenReturn(false);
        Mockito.when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("hashedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(usuarioEjemplo());

        User resultado = userService.registerSimple(
                RUN_SUCIO, "Juan", "Pérez", "juan@example.com", "password123", "ADMIN"
        );


        assertNotNull(resultado);
        assertEquals("USER", resultado.getRole());

        verify(userRepository).save(Mockito.any(User.class));
    }

    // ── findByRun ─────────────────────────────────────────────────────────────

    @Test
    void deberiaBuscarUsuarioPorRunCuandoExiste() {
        Mockito.when(userRepository.findById(RUN_LIMPIO))
                .thenReturn(Optional.of(usuarioEjemplo()));

        Optional<User> resultado = userService.findByRun(RUN_SUCIO);

        assertTrue(resultado.isPresent());
        assertEquals(RUN_LIMPIO, resultado.get().getRun());

        verify(userRepository).findById(RUN_LIMPIO);
    }

    @Test
    void deberiaRetornarVacioCuandoRunNoExiste() {
        Mockito.when(userRepository.findById(RUN_LIMPIO)).thenReturn(Optional.empty());

        Optional<User> resultado = userService.findByRun(RUN_SUCIO);

        assertTrue(resultado.isEmpty());
        verify(userRepository).findById(RUN_LIMPIO);
    }

    // ── findByNombre ──────────────────────────────────────────────────────────

    @Test
    void deberiaBuscarUsuarioPorNombreCuandoExiste() {
        Mockito.when(userRepository.findByNombre("Juan"))
                .thenReturn(Optional.of(usuarioEjemplo()));

        Optional<User> resultado = userService.findByNombre("Juan");

        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombre());

        verify(userRepository).findByNombre("Juan");
    }

    @Test
    void deberiaRetornarVacioCuandoNombreNoExiste() {
        Mockito.when(userRepository.findByNombre("Inexistente")).thenReturn(Optional.empty());

        Optional<User> resultado = userService.findByNombre("Inexistente");

        assertTrue(resultado.isEmpty());
        verify(userRepository).findByNombre("Inexistente");
    }

    // ── update ────────────────────────────────────────────────────────────────

    @Test
    void deberiaActualizarUsuarioCuandoExisteYEmailLibre() {
        User existente = usuarioEjemplo();

        User datosNuevos = User.builder()
                .nombre("Juan Actualizado")
                .apellido("López")
                .email("nuevo@example.com")
                .password("nuevaPass")
                .build();

        User actualizado = User.builder()
                .run(RUN_LIMPIO)
                .nombre("Juan Actualizado")
                .apellido("López")
                .email("nuevo@example.com")
                .password("hashedNueva")
                .role("USER")
                .build();

        Mockito.when(userRepository.findById(RUN_LIMPIO)).thenReturn(Optional.of(existente));
        Mockito.when(userRepository.findByEmail("nuevo@example.com")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("nuevaPass")).thenReturn("hashedNueva");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(actualizado);

        User resultado = userService.update(RUN_SUCIO, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombre());
        assertEquals("nuevo@example.com", resultado.getEmail());

        verify(userRepository).findById(RUN_LIMPIO);
        verify(userRepository).findByEmail("nuevo@example.com");
        verify(userRepository).save(existente);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarUsuarioInexistente() {
        Mockito.when(userRepository.findById(RUN_LIMPIO)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.update(RUN_SUCIO, new User())
        );

        assertEquals("Usuario no encontrado.", ex.getMessage());
        verify(userRepository).findById(RUN_LIMPIO);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarConEmailDuplicadoDeTercero() {
        User existente = usuarioEjemplo();

        User otroUsuario = User.builder()
                .run("999999999")
                .email("ocupado@example.com")
                .build();

        User datosNuevos = User.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email("ocupado@example.com")
                .build();

        Mockito.when(userRepository.findById(RUN_LIMPIO)).thenReturn(Optional.of(existente));
        Mockito.when(userRepository.findByEmail("ocupado@example.com"))
                .thenReturn(Optional.of(otroUsuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.update(RUN_SUCIO, datosNuevos)
        );

        assertTrue(ex.getMessage().contains("ocupado@example.com"));
        verify(userRepository).findById(RUN_LIMPIO);
        verify(userRepository).findByEmail("ocupado@example.com");
    }

    // ── delete ────────────────────────────────────────────────────────────────

    @Test
    void deberiaEliminarUsuarioCuandoExiste() {
        Mockito.when(userRepository.existsById(RUN_LIMPIO)).thenReturn(true);

        assertDoesNotThrow(() -> userService.delete(RUN_SUCIO));

        verify(userRepository).existsById(RUN_LIMPIO);
        verify(userRepository).deleteById(RUN_LIMPIO);
    }

    @Test
    void deberiaLanzarExcepcionAlEliminarUsuarioInexistente() {
        Mockito.when(userRepository.existsById(RUN_LIMPIO)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.delete(RUN_SUCIO)
        );

        assertEquals("No se puede eliminar.", ex.getMessage());
        verify(userRepository).existsById(RUN_LIMPIO);
    }
}
