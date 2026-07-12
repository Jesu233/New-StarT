package com.example.Reservas;

import com.example.Reservas.model.Reservas;
import com.example.Reservas.repository.ReservasRepository;
import com.example.Reservas.service.ReservasService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservasServiceTest {

    @Mock
    private ReservasRepository reservasRepository;

    @InjectMocks
    private ReservasService reservasService;


    private Reservas reservaEjemplo() {
        Reservas r = new Reservas();
        r.setId(1L);
        r.setNombre("Reserva Test");
        r.setFechaInicio(LocalDateTime.of(2026, 8, 15, 10, 0));
        r.setFechaFin(LocalDateTime.of(2026, 8, 15, 12, 0));
        r.setTipo("VIP");
        r.setCliente("Juan Pérez");
        r.setEstado("CONFIRMADA");
        return r;
    }


    @Test
    void deberiaGuardarReserva() {
        Reservas reserva = reservaEjemplo();

        Mockito.when(reservasRepository.save(Mockito.any(Reservas.class)))
                .thenReturn(reserva);

        Reservas resultado = reservasService.guardarReserva(reserva);

        assertNotNull(resultado);
        assertEquals("Reserva Test", resultado.getNombre());
        assertEquals("Juan Pérez", resultado.getCliente());

        verify(reservasRepository).save(reserva);
    }


    @Test
    void deberiaEliminarReservaCuandoExiste() {
        Mockito.when(reservasRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> reservasService.eliminarReserva(1L));

        verify(reservasRepository).existsById(1L);
        verify(reservasRepository).deleteById(1L);
    }

    @Test
    void deberiaLanzarExcepcionAlEliminarReservaInexistente() {
        Mockito.when(reservasRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reservasService.eliminarReserva(99L));

        assertEquals("No se ha encontrado la reserva.", ex.getMessage());

        verify(reservasRepository).existsById(99L);
    }

    @Test
    void deberiaActualizarReservaCuandoExiste() {
        Reservas existente = reservaEjemplo();

        Reservas datosNuevos = new Reservas();
        datosNuevos.setId(1L);
        datosNuevos.setNombre("Reserva Actualizada");
        datosNuevos.setFechaInicio(LocalDateTime.of(2026, 9, 1, 10, 0));
        datosNuevos.setFechaFin(LocalDateTime.of(2026, 9, 1, 12, 0));
        datosNuevos.setTipo("GENERAL");
        datosNuevos.setCliente("Ana Gómez");
        datosNuevos.setEstado("PENDIENTE");

        Mockito.when(reservasRepository.existsById(1L)).thenReturn(true);
        Mockito.when(reservasRepository.save(Mockito.any(Reservas.class)))
                .thenReturn(datosNuevos);

        Reservas resultado = reservasService.actualizarReserva(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Reserva Actualizada", resultado.getNombre());

        verify(reservasRepository).existsById(1L);
        verify(reservasRepository).save(datosNuevos);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarReservaInexistente() {
        Reservas datosNuevos = new Reservas();
        datosNuevos.setNombre("No existe");

        Mockito.when(reservasRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reservasService.actualizarReserva(99L, datosNuevos));

        assertEquals("No se ha encontrado la reserva", ex.getMessage());

        verify(reservasRepository).existsById(99L);
    }

    @Test
    void deberiaBuscarReservaPorIdCuandoExiste() {
        Reservas reserva = reservaEjemplo();

        Mockito.when(reservasRepository.existsById(1L)).thenReturn(true);
        Mockito.when(reservasRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Reservas resultado = reservasService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Reserva Test", resultado.getNombre());

        verify(reservasRepository).existsById(1L);
        verify(reservasRepository).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionAlBuscarPorIdInexistente() {
        Mockito.when(reservasRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reservasService.buscarPorId(99L));

        assertEquals("No se ha encontrado la reserva", ex.getMessage());

        verify(reservasRepository).existsById(99L);
    }

    @Test
    void deberiaBuscarReservaPorNombreCuandoExiste() {
        Reservas reserva = reservaEjemplo();

        Mockito.when(reservasRepository.findByNombre("Reserva Test"))
                .thenReturn(Optional.of(reserva));

        Reservas resultado = reservasService.buscarPorNombre("Reserva Test");

        assertNotNull(resultado);
        assertEquals("Reserva Test", resultado.getNombre());

        verify(reservasRepository).findByNombre("Reserva Test");
    }

    @Test
    void deberiaLanzarExcepcionAlBuscarPorNombreInexistente() {
        Mockito.when(reservasRepository.findByNombre("Inexistente"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reservasService.buscarPorNombre("Inexistente"));

        assertEquals("No se ha encontrado la reserva", ex.getMessage());

        verify(reservasRepository).findByNombre("Inexistente");
    }
}