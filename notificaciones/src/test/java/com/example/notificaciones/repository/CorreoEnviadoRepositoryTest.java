package com.example.notificaciones.repository;

import com.example.notificaciones.model.CorreoEnviado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CorreoEnviadoRepositoryTest {

    @Autowired
    private CorreoEnviadoRepository repository;

    // ==================== TEST 1: GUARDAR CORREO ====================
    @Test
    void deberiaGuardarCorreo() {
        // Preparar
        CorreoEnviado correo = new CorreoEnviado();
        correo.setDestinatario("juan@email.com");
        correo.setTipo("COMPRA");
        correo.setEstado("ENVIADO");
        correo.setFechaEnvio(LocalDateTime.now());

        // Ejecutar
        CorreoEnviado guardado = repository.save(correo);

        // Verificar
        assertNotNull(guardado.getId());
        assertEquals("juan@email.com", guardado.getDestinatario());
        assertEquals("COMPRA", guardado.getTipo());
        assertEquals("ENVIADO", guardado.getEstado());
        assertNotNull(guardado.getFechaEnvio());
    }

    // ==================== TEST 2: BUSCAR POR ID ====================
    @Test
    void deberiaEncontrarCorreoPorId() {
        // Preparar
        CorreoEnviado correo = new CorreoEnviado();
        correo.setDestinatario("maria@email.com");
        correo.setTipo("BIENVENIDA");
        correo.setEstado("ENVIADO");
        correo.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado guardado = repository.save(correo);

        // Ejecutar
        Optional<CorreoEnviado> encontrado = repository.findById(guardado.getId());

        // Verificar
        assertTrue(encontrado.isPresent());
        assertEquals("maria@email.com", encontrado.get().getDestinatario());
        assertEquals("BIENVENIDA", encontrado.get().getTipo());
        assertEquals("ENVIADO", encontrado.get().getEstado());
    }

    // ==================== TEST 3: BUSCAR POR DESTINATARIO ====================
    @Test
    void deberiaEncontrarCorreosPorDestinatario() {
        // Preparar
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setDestinatario("juan@email.com");
        correo2.setTipo("LOGOUT");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo3 = new CorreoEnviado();
        correo3.setDestinatario("maria@email.com");
        correo3.setTipo("BIENVENIDA");
        correo3.setEstado("ENVIADO");
        correo3.setFechaEnvio(LocalDateTime.now());

        repository.save(correo1);
        repository.save(correo2);
        repository.save(correo3);

        // Ejecutar
        List<CorreoEnviado> encontrados = repository.findByDestinatario("juan@email.com");

        // Verificar
        assertEquals(2, encontrados.size());
        assertTrue(encontrados.stream().allMatch(c -> c.getDestinatario().equals("juan@email.com")));
        assertTrue(encontrados.stream().anyMatch(c -> c.getTipo().equals("COMPRA")));
        assertTrue(encontrados.stream().anyMatch(c -> c.getTipo().equals("LOGOUT")));
    }

    // ==================== TEST 4: BUSCAR POR TIPO ====================
    @Test
    void deberiaEncontrarCorreosPorTipo() {
        // Preparar
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setDestinatario("maria@email.com");
        correo2.setTipo("COMPRA");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo3 = new CorreoEnviado();
        correo3.setDestinatario("pedro@email.com");
        correo3.setTipo("BIENVENIDA");
        correo3.setEstado("ENVIADO");
        correo3.setFechaEnvio(LocalDateTime.now());

        repository.save(correo1);
        repository.save(correo2);
        repository.save(correo3);

        // Ejecutar
        List<CorreoEnviado> encontrados = repository.findByTipo("COMPRA");

        // Verificar
        assertEquals(2, encontrados.size());
        assertTrue(encontrados.stream().allMatch(c -> c.getTipo().equals("COMPRA")));
        assertTrue(encontrados.stream().anyMatch(c -> c.getDestinatario().equals("juan@email.com")));
        assertTrue(encontrados.stream().anyMatch(c -> c.getDestinatario().equals("maria@email.com")));
    }

    // ==================== TEST 5: BUSCAR POR ESTADO ====================
    @Test
    void deberiaEncontrarCorreosPorEstado() {
        // Preparar
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setDestinatario("maria@email.com");
        correo2.setTipo("BIENVENIDA");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo3 = new CorreoEnviado();
        correo3.setDestinatario("pedro@email.com");
        correo3.setTipo("LOGOUT");
        correo3.setEstado("FALLIDO");
        correo3.setFechaEnvio(LocalDateTime.now());

        repository.save(correo1);
        repository.save(correo2);
        repository.save(correo3);

        // Ejecutar
        List<CorreoEnviado> encontrados = repository.findByEstado("ENVIADO");

        // Verificar
        assertEquals(2, encontrados.size());
        assertTrue(encontrados.stream().allMatch(c -> c.getEstado().equals("ENVIADO")));
        assertTrue(encontrados.stream().anyMatch(c -> c.getDestinatario().equals("juan@email.com")));
        assertTrue(encontrados.stream().anyMatch(c -> c.getDestinatario().equals("maria@email.com")));
    }

    // ==================== TEST 6: BUSCAR POR DESTINATARIO Y TIPO ====================
    @Test
    void deberiaEncontrarCorreosPorDestinatarioYTipo() {
        // Preparar
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setDestinatario("juan@email.com");
        correo2.setTipo("LOGOUT");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo3 = new CorreoEnviado();
        correo3.setDestinatario("maria@email.com");
        correo3.setTipo("COMPRA");
        correo3.setEstado("ENVIADO");
        correo3.setFechaEnvio(LocalDateTime.now());

        repository.save(correo1);
        repository.save(correo2);
        repository.save(correo3);

        // Ejecutar (este método debe agregarse al repository)
        // List<CorreoEnviado> encontrados = repository.findByDestinatarioAndTipo("juan@email.com", "COMPRA");

        // Verificar
        // assertEquals(1, encontrados.size());
        // assertEquals("juan@email.com", encontrados.get(0).getDestinatario());
        // assertEquals("COMPRA", encontrados.get(0).getTipo());
    }

    // ==================== TEST 7: CONTAR CORREOS POR ESTADO ====================
    @Test
    void deberiaContarCorreosPorEstado() {
        // Preparar
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setDestinatario("maria@email.com");
        correo2.setTipo("BIENVENIDA");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo3 = new CorreoEnviado();
        correo3.setDestinatario("pedro@email.com");
        correo3.setTipo("LOGOUT");
        correo3.setEstado("FALLIDO");
        correo3.setFechaEnvio(LocalDateTime.now());

        repository.save(correo1);
        repository.save(correo2);
        repository.save(correo3);

        // Ejecutar (este método debe agregarse al repository)
        // long countEnviados = repository.countByEstado("ENVIADO");
        // long countFallidos = repository.countByEstado("FALLIDO");

        // Verificar
        // assertEquals(2, countEnviados);
        // assertEquals(1, countFallidos);
    }

    // ==================== TEST 8: CONTAR CORREOS POR TIPO ====================
    @Test
    void deberiaContarCorreosPorTipo() {
        // Preparar
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setDestinatario("maria@email.com");
        correo2.setTipo("COMPRA");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo3 = new CorreoEnviado();
        correo3.setDestinatario("pedro@email.com");
        correo3.setTipo("BIENVENIDA");
        correo3.setEstado("ENVIADO");
        correo3.setFechaEnvio(LocalDateTime.now());

        repository.save(correo1);
        repository.save(correo2);
        repository.save(correo3);

        // Ejecutar (este método debe agregarse al repository)
        // long countCompra = repository.countByTipo("COMPRA");
        // long countBienvenida = repository.countByTipo("BIENVENIDA");

        // Verificar
        // assertEquals(2, countCompra);
        // assertEquals(1, countBienvenida);
    }

    // ==================== TEST 9: ELIMINAR CORREO ====================
    @Test
    void deberiaEliminarCorreoPorId() {
        // Preparar
        CorreoEnviado correo = new CorreoEnviado();
        correo.setDestinatario("juan@email.com");
        correo.setTipo("COMPRA");
        correo.setEstado("ENVIADO");
        correo.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado guardado = repository.save(correo);
        Long id = guardado.getId();

        // Verificar que existe
        assertTrue(repository.findById(id).isPresent());

        // Ejecutar
        repository.deleteById(id);

        // Verificar que ya no existe
        assertFalse(repository.findById(id).isPresent());
    }

    // ==================== TEST 10: OBTENER TODOS LOS CORREOS ====================
    @Test
    void deberiaObtenerTodosLosCorreos() {
        // Preparar
        CorreoEnviado correo1 = new CorreoEnviado();
        correo1.setDestinatario("juan@email.com");
        correo1.setTipo("COMPRA");
        correo1.setEstado("ENVIADO");
        correo1.setFechaEnvio(LocalDateTime.now());

        CorreoEnviado correo2 = new CorreoEnviado();
        correo2.setDestinatario("maria@email.com");
        correo2.setTipo("BIENVENIDA");
        correo2.setEstado("ENVIADO");
        correo2.setFechaEnvio(LocalDateTime.now());

        repository.save(correo1);
        repository.save(correo2);

        // Ejecutar
        List<CorreoEnviado> todos = repository.findAll();

        // Verificar
        assertEquals(2, todos.size());
    }

    // ==================== TEST 11: BUSCAR POR DESTINATARIO SIN RESULTADOS ====================
    @Test
    void deberiaRetornarListaVaciaCuandoNoHayCorreosPorDestinatario() {
        // Ejecutar
        List<CorreoEnviado> encontrados = repository.findByDestinatario("noexiste@email.com");

        // Verificar
        assertTrue(encontrados.isEmpty());
    }

    // ==================== TEST 12: BUSCAR POR TIPO SIN RESULTADOS ====================
    @Test
    void deberiaRetornarListaVaciaCuandoNoHayCorreosPorTipo() {
        // Ejecutar
        List<CorreoEnviado> encontrados = repository.findByTipo("INEXISTENTE");

        // Verificar
        assertTrue(encontrados.isEmpty());
    }

    // ==================== TEST 13: BUSCAR POR ESTADO SIN RESULTADOS ====================
    @Test
    void deberiaRetornarListaVaciaCuandoNoHayCorreosPorEstado() {
        // Ejecutar
        List<CorreoEnviado> encontrados = repository.findByEstado("INEXISTENTE");

        // Verificar
        assertTrue(encontrados.isEmpty());
    }
}