package com.example.localidades.service;

import com.example.localidades.model.Estadisticas;
import com.example.localidades.model.Localidades;
import com.example.localidades.repository.LocalidadesRepository;
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
class LocalidadesServiceTest {

    @Mock
    private LocalidadesRepository repository;

    @InjectMocks
    private LocalidadesService service;

    private Localidades crearLocalidad(Long id, String nombre, Long campoDelantero, Long campoTrasero,
                                       Long plateaBaja, Long plateaAlta, Long recintoId) {
        Localidades localidad = new Localidades();
        localidad.setId(id);
        localidad.setNombre(nombre);
        localidad.setCampoDelantero(campoDelantero);
        localidad.setCampoTrasero(campoTrasero);
        localidad.setPlateaBaja(plateaBaja);
        localidad.setPlateaAlta(plateaAlta);
        localidad.setRecintoId(recintoId);
        return localidad;
    }

    @Test
    void deberiaRetornarLocalidadCuandoExiste() {

        Localidades localidad = crearLocalidad(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(localidad));

        Optional<Localidades> resultado = service.obtener(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Platea Norte", resultado.get().getNombre());

        verify(repository).findById(1L);
    }

    @Test
    void deberiaRetornarVacioCuandoLocalidadNoExiste() {

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Localidades> resultado = service.obtener(99L);

        assertTrue(resultado.isEmpty());

        verify(repository).findById(99L);
    }

    @Test
    void deberiaListarTodasLasLocalidades() {

        Localidades localidad1 = crearLocalidad(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);
        Localidades localidad2 = crearLocalidad(2L, "Platea Sur", 1200L, 1200L, 700L, 500L, 4L);

        Mockito.when(repository.findAll())
                .thenReturn(List.of(localidad1, localidad2));

        List<Localidades> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertEquals("Platea Norte", resultado.get(0).getNombre());

        verify(repository).findAll();
    }

    @Test
    void deberiaBuscarPorRecintoIdCuandoExiste() {

        Localidades localidad = crearLocalidad(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        Mockito.when(repository.findByRecintoId(3L))
                .thenReturn(Optional.of(localidad));

        Optional<Localidades> resultado = service.obtenerPorRecintoId(3L);

        assertTrue(resultado.isPresent());
        assertEquals("Platea Norte", resultado.get().getNombre());

        verify(repository).findByRecintoId(3L);
    }

    @Test
    void deberiaRetornarVacioCuandoRecintoIdNoExiste() {

        Mockito.when(repository.findByRecintoId(99L))
                .thenReturn(Optional.empty());

        Optional<Localidades> resultado = service.obtenerPorRecintoId(99L);

        assertTrue(resultado.isEmpty());

        verify(repository).findByRecintoId(99L);
    }

    @Test
    void deberiaBuscarPorNombre() {

        Localidades localidad = crearLocalidad(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        Mockito.when(repository.findByNombreContainingIgnoreCase("platea"))
                .thenReturn(List.of(localidad));

        List<Localidades> resultado = service.buscarPorNombre("platea");

        assertEquals(1, resultado.size());
        assertEquals("Platea Norte", resultado.get(0).getNombre());

        verify(repository).findByNombreContainingIgnoreCase("platea");
    }

    @Test
    void deberiaGuardarLocalidad() {

        Localidades localidad = crearLocalidad(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        Mockito.when(repository.save(Mockito.any(Localidades.class)))
                .thenReturn(localidad);

        Localidades resultado = service.guardar(localidad);

        assertNotNull(resultado);
        assertEquals("Platea Norte", resultado.getNombre());

        verify(repository).save(localidad);
    }

    @Test
    void deberiaActualizarLocalidadCuandoExiste() {

        Localidades existente = crearLocalidad(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        Localidades datosNuevos = crearLocalidad(null, "Platea Norte Renovada", 1600L, 1600L, 850L, 650L, 3L);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existente));
        Mockito.when(repository.save(Mockito.any(Localidades.class)))
                .thenReturn(existente);

        Optional<Localidades> resultado = service.actualizar(1L, datosNuevos);

        assertTrue(resultado.isPresent());
        assertEquals("Platea Norte Renovada", resultado.get().getNombre());
        assertEquals(1600L, resultado.get().getCampoDelantero());

        verify(repository).findById(1L);
        verify(repository).save(existente);
    }

    @Test
    void deberiaRetornarVacioAlActualizarLocalidadInexistente() {

        Localidades datosNuevos = crearLocalidad(null, "No existe", 1000L, 1000L, 500L, 400L, 3L);

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Localidades> resultado = service.actualizar(99L, datosNuevos);

        assertTrue(resultado.isEmpty());

        verify(repository).findById(99L);
    }

    @Test
    void deberiaEliminarLocalidadCuandoExiste() {

        Localidades localidad = crearLocalidad(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(localidad));

        boolean resultado = service.eliminar(1L);

        assertTrue(resultado);

        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deberiaRetornarFalseAlEliminarLocalidadInexistente() {

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        boolean resultado = service.eliminar(99L);

        assertFalse(resultado);

        verify(repository).findById(99L);
    }

    @Test
    void deberiaCalcularCapacidadTotalCuandoExiste() {

        Localidades localidad = crearLocalidad(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(localidad));

        Optional<Long> resultado = service.calcularCapacidadTotal(1L);

        assertTrue(resultado.isPresent());
        assertEquals(4400L, resultado.get());

        verify(repository).findById(1L);
    }

    @Test
    void deberiaRetornarVacioAlCalcularCapacidadTotalDeLocalidadInexistente() {

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Long> resultado = service.calcularCapacidadTotal(99L);

        assertTrue(resultado.isEmpty());

        verify(repository).findById(99L);
    }

    @Test
    void deberiaObtenerEstadisticasConLocalidadesRegistradas() {

        Localidades localidad1 = crearLocalidad(1L, "Platea Norte", 1500L, 1500L, 800L, 600L, 3L);
        // total = 4400
        Localidades localidad2 = crearLocalidad(2L, "Platea Sur", 1000L, 1000L, 500L, 400L, 4L);
        // total = 2900

        Mockito.when(repository.findAll())
                .thenReturn(List.of(localidad1, localidad2));

        Estadisticas resultado = service.obtenerEstadisticas();

        assertEquals(2L, resultado.getTotalLocalidades());
        assertEquals(4400L, resultado.getMayorCapacidad());
        assertEquals(2900L, resultado.getMenorCapacidad());
        assertEquals(3650L, resultado.getCapacidadPromedio());

        verify(repository).findAll();
    }

    @Test
    void deberiaObtenerEstadisticasCuandoNoHayLocalidades() {

        Mockito.when(repository.findAll())
                .thenReturn(List.of());

        Estadisticas resultado = service.obtenerEstadisticas();

        assertEquals(0L, resultado.getTotalLocalidades());
        assertEquals(0L, resultado.getMayorCapacidad());
        assertEquals(0L, resultado.getMenorCapacidad());
        assertEquals(0L, resultado.getCapacidadPromedio());

        verify(repository).findAll();
    }
}