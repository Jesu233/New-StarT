package com.example.recintos.service;

import com.example.recintos.model.Recintos;
import com.example.recintos.repository.RecintosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecintosServiceTest {

    @Mock
    private RecintosRepository repository;

    @InjectMocks
    private RecintosService service;

    @Test
    void deberiaRetornarRecintoCuandoExiste() {

        Recintos recinto = new Recintos();
        recinto.setId(1L);
        recinto.setNombre("Movistar Arena");
        recinto.setUbicacion("Santiago");
        recinto.setCapacidad(15500L);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(recinto));

        Optional<Recintos> resultado = service.obtener(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Movistar Arena", resultado.get().getNombre());

        verify(repository).findById(1L);
    }

    @Test
    void deberiaRetornarVacioCuandoRecintoNoExiste() {

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Recintos> resultado = service.obtener(99L);

        assertTrue(resultado.isEmpty());

        verify(repository).findById(99L);
    }

    @Test
    void deberiaListarTodosLosRecintos() {

        Recintos recinto1 = new Recintos();
        recinto1.setId(1L);
        recinto1.setNombre("Movistar Arena");

        Recintos recinto2 = new Recintos();
        recinto2.setId(2L);
        recinto2.setNombre("Estadio Nacional");

        Mockito.when(repository.findAll())
                .thenReturn(List.of(recinto1, recinto2));

        List<Recintos> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertEquals("Movistar Arena", resultado.get(0).getNombre());

        verify(repository).findAll();
    }

    @Test
    void deberiaBuscarPorNombre() {

        Recintos recinto = new Recintos();
        recinto.setId(1L);
        recinto.setNombre("Movistar Arena");

        Mockito.when(repository.findByNombreContainingIgnoreCase("movistar"))
                .thenReturn(List.of(recinto));

        List<Recintos> resultado = service.buscarPorNombre("movistar");

        assertEquals(1, resultado.size());
        assertEquals("Movistar Arena", resultado.get(0).getNombre());

        verify(repository).findByNombreContainingIgnoreCase("movistar");
    }

    @Test
    void deberiaGuardarRecinto() {

        Recintos recinto = new Recintos();
        recinto.setId(1L);
        recinto.setNombre("Movistar Arena");
        recinto.setUbicacion("Santiago");
        recinto.setCapacidad(15500L);

        Mockito.when(repository.save(Mockito.any(Recintos.class)))
                .thenReturn(recinto);

        Recintos resultado = service.guardar(recinto);

        assertNotNull(resultado);
        assertEquals("Movistar Arena", resultado.getNombre());

        verify(repository).save(recinto);
    }

    @Test
    void deberiaActualizarRecintoCuandoExiste() {

        Recintos existente = new Recintos();
        existente.setId(1L);
        existente.setNombre("Movistar Arena");
        existente.setUbicacion("Santiago");
        existente.setCapacidad(15500L);

        Recintos datosNuevos = new Recintos();
        datosNuevos.setNombre("Movistar Arena Updated");
        datosNuevos.setUbicacion("Santiago Centro");
        datosNuevos.setCapacidad(16000L);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existente));
        Mockito.when(repository.save(Mockito.any(Recintos.class)))
                .thenReturn(existente);

        Optional<Recintos> resultado = service.actualizar(1L, datosNuevos);

        assertTrue(resultado.isPresent());
        assertEquals("Movistar Arena Updated", resultado.get().getNombre());

        verify(repository).findById(1L);
        verify(repository).save(existente);
    }

    @Test
    void deberiaRetornarVacioAlActualizarRecintoInexistente() {

        Recintos datosNuevos = new Recintos();
        datosNuevos.setNombre("No existe");

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Recintos> resultado = service.actualizar(99L, datosNuevos);

        assertTrue(resultado.isEmpty());

        verify(repository).findById(99L);
    }

    @Test
    void deberiaEliminarRecintoCuandoExiste() {

        Recintos recinto = new Recintos();
        recinto.setId(1L);
        recinto.setNombre("Movistar Arena");

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(recinto));

        boolean resultado = service.eliminar(1L);

        assertTrue(resultado);

        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deberiaRetornarFalseAlEliminarRecintoInexistente() {

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        boolean resultado = service.eliminar(99L);

        assertFalse(resultado);

        verify(repository).findById(99L);
    }
}