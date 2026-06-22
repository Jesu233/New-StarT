package com.example.artistas.service;

import com.example.artistas.model.Artistas;
import com.example.artistas.repository.ArtistasRepository;
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
class ArtistasServiceTest {

    @Mock
    private ArtistasRepository repository;

    @InjectMocks
    private ArtistasService service;

    @Test
    void deberiaRetornarArtistaCuandoExiste() {

        Artistas artista = new Artistas();
        artista.setId(1L);
        artista.setNombre("Coldplay");
        artista.setGenero("Pop Rock");
        artista.setPais("Reino Unido");

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(artista));

        Optional<Artistas> resultado = service.obtener(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Coldplay", resultado.get().getNombre());

        verify(repository).findById(1L);
    }

    @Test
    void deberiaRetornarVacioCuandoArtistaNoExiste() {

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Artistas> resultado = service.obtener(99L);

        assertTrue(resultado.isEmpty());

        verify(repository).findById(99L);
    }

    @Test
    void deberiaListarTodosLosArtistas() {

        Artistas artista1 = new Artistas();
        artista1.setId(1L);
        artista1.setNombre("Coldplay");

        Artistas artista2 = new Artistas();
        artista2.setId(2L);
        artista2.setNombre("Bad Bunny");

        Mockito.when(repository.findAll())
                .thenReturn(List.of(artista1, artista2));

        List<Artistas> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertEquals("Coldplay", resultado.get(0).getNombre());

        verify(repository).findAll();
    }

    @Test
    void deberiaBuscarPorNombre() {

        Artistas artista = new Artistas();
        artista.setId(1L);
        artista.setNombre("Coldplay");

        Mockito.when(repository.findByNombreContainingIgnoreCase("cold"))
                .thenReturn(List.of(artista));

        List<Artistas> resultado = service.buscarPorNombre("cold");

        assertEquals(1, resultado.size());
        assertEquals("Coldplay", resultado.get(0).getNombre());

        verify(repository).findByNombreContainingIgnoreCase("cold");
    }

    @Test
    void deberiaBuscarPorGenero() {

        Artistas artista = new Artistas();
        artista.setId(1L);
        artista.setNombre("Metallica");
        artista.setGenero("Heavy Metal");

        Mockito.when(repository.findByGeneroContainingIgnoreCase("metal"))
                .thenReturn(List.of(artista));

        List<Artistas> resultado = service.buscarPorGenero("metal");

        assertEquals(1, resultado.size());
        assertEquals("Metallica", resultado.get(0).getNombre());

        verify(repository).findByGeneroContainingIgnoreCase("metal");
    }

    @Test
    void deberiaGuardarArtista() {

        Artistas artista = new Artistas();
        artista.setId(1L);
        artista.setNombre("Coldplay");
        artista.setGenero("Pop Rock");
        artista.setPais("Reino Unido");

        Mockito.when(repository.save(Mockito.any(Artistas.class)))
                .thenReturn(artista);

        Artistas resultado = service.guardar(artista);

        assertNotNull(resultado);
        assertEquals("Coldplay", resultado.getNombre());

        verify(repository).save(artista);
    }

    @Test
    void deberiaActualizarArtistaCuandoExiste() {

        Artistas existente = new Artistas();
        existente.setId(1L);
        existente.setNombre("Coldplay");
        existente.setGenero("Pop Rock");
        existente.setPais("Reino Unido");

        Artistas datosNuevos = new Artistas();
        datosNuevos.setNombre("Coldplay Updated");
        datosNuevos.setGenero("Alternative Rock");
        datosNuevos.setPais("UK");

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existente));
        Mockito.when(repository.save(Mockito.any(Artistas.class)))
                .thenReturn(existente);

        Optional<Artistas> resultado = service.actualizar(1L, datosNuevos);

        assertTrue(resultado.isPresent());
        assertEquals("Coldplay Updated", resultado.get().getNombre());

        verify(repository).findById(1L);
        verify(repository).save(existente);
    }

    @Test
    void deberiaRetornarVacioAlActualizarArtistaInexistente() {

        Artistas datosNuevos = new Artistas();
        datosNuevos.setNombre("No existe");

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Artistas> resultado = service.actualizar(99L, datosNuevos);

        assertTrue(resultado.isEmpty());

        verify(repository).findById(99L);
    }

    @Test
    void deberiaEliminarArtistaCuandoExiste() {

        Artistas artista = new Artistas();
        artista.setId(1L);
        artista.setNombre("Coldplay");

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(artista));

        boolean resultado = service.eliminar(1L);

        assertTrue(resultado);

        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deberiaRetornarFalseAlEliminarArtistaInexistente() {

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        boolean resultado = service.eliminar(99L);

        assertFalse(resultado);

        verify(repository).findById(99L);
    }
}