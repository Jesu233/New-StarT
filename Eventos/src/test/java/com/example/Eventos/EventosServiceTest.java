package com.example.Eventos;

import com.example.Eventos.model.Eventos;
import com.example.Eventos.repository.EventosRepository;
import com.example.Eventos.service.EventosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventosService - pruebas unitarias")
class EventosServiceTest {

    @Mock
    private EventosRepository eventosRepository;

    @InjectMocks
    private EventosService eventosService;

    private Eventos evento;

    @BeforeEach
    void setUp() {
        evento = new Eventos();
        evento.setId(1L);
        evento.setNombre("Cosmódromo");
        evento.setCapacidad(4000L);
        evento.setFechaEvento(LocalDateTime.of(2026, 8, 15, 20, 0, 0));
        evento.setLugarEvento("Estadio Nacional");
    }

    @Nested
    @DisplayName("agregarEvento")
    class AgregarEvento {

        @Test
        @DisplayName("debería guardar y retornar el evento creado")
        void deberiaGuardarEvento() {
            when(eventosRepository.save(evento)).thenReturn(evento);

            Eventos resultado = eventosService.agregarEvento(evento);

            assertThat(resultado).isEqualTo(evento);
            verify(eventosRepository, times(1)).save(evento);
            verifyNoMoreInteractions(eventosRepository);
        }
    }

    @Nested
    @DisplayName("eliminarEvento")
    class EliminarEvento {

        @Test
        @DisplayName("debería eliminar el evento cuando existe")
        void deberiaEliminarEventoExistente() {
            when(eventosRepository.findById(1L)).thenReturn(Optional.of(evento));

            eventosService.eliminarEvento(1L);

            verify(eventosRepository).findById(1L);
            verify(eventosRepository).delete(evento);
        }

        @Test
        @DisplayName("debería lanzar excepción cuando el evento no existe")
        void deberiaLanzarExcepcionSiNoExiste() {
            when(eventosRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eventosService.eliminarEvento(99L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("No se ha encontrado el evento");

            verify(eventosRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("actualizarEvento")
    class ActualizarEvento {

        @Test
        @DisplayName("debería actualizar los datos de un evento existente")
        void deberiaActualizarEventoExistente() {
            Eventos datosNuevos = new Eventos();
            datosNuevos.setNombre("Festival de Verano");
            datosNuevos.setCapacidad(6000L);
            datosNuevos.setFechaEvento(LocalDateTime.of(2027, 1, 10, 18, 0, 0));
            datosNuevos.setLugarEvento("Parque O'Higgins");

            when(eventosRepository.findById(1L)).thenReturn(Optional.of(evento));
            when(eventosRepository.save(any(Eventos.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Eventos resultado = eventosService.actualizarEvento(1L, datosNuevos);

            assertThat(resultado.getNombre()).isEqualTo("Festival de Verano");
            assertThat(resultado.getCapacidad()).isEqualTo(6000L);
            assertThat(resultado.getLugarEvento()).isEqualTo("Parque O'Higgins");
            assertThat(resultado.getFechaEvento()).isEqualTo(datosNuevos.getFechaEvento());
            verify(eventosRepository).save(evento);
        }

        @Test
        @DisplayName("debería lanzar excepción cuando el evento a actualizar no existe")
        void deberiaLanzarExcepcionSiNoExisteAlActualizar() {
            when(eventosRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eventosService.actualizarEvento(99L, evento))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("No se ha encontrado el evento");

            verify(eventosRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("buscarPorId")
    class BuscarPorId {

        @Test
        @DisplayName("debería retornar el evento cuando existe")
        void deberiaRetornarEventoExistente() {
            when(eventosRepository.findById(1L)).thenReturn(Optional.of(evento));

            Eventos resultado = eventosService.buscarPorId(1L);

            assertThat(resultado).isEqualTo(evento);
        }

        @Test
        @DisplayName("debería lanzar excepción cuando no existe")
        void deberiaLanzarExcepcionSiNoExiste() {
            when(eventosRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eventosService.buscarPorId(99L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("No se encontró el evento");
        }
    }

    @Nested
    @DisplayName("buscarPorNombre")
    class BuscarPorNombre {

        @Test
        @DisplayName("debería retornar el evento cuando el nombre existe")
        void deberiaRetornarEventoPorNombre() {
            when(eventosRepository.findByNombre("Cosmódromo")).thenReturn(Optional.of(evento));

            Eventos resultado = eventosService.buscarPorNombre("Cosmódromo");

            assertThat(resultado).isEqualTo(evento);
        }

        @Test
        @DisplayName("debería lanzar excepción cuando el nombre no existe")
        void deberiaLanzarExcepcionSiNombreNoExiste() {
            when(eventosRepository.findByNombre("Inexistente")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eventosService.buscarPorNombre("Inexistente"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("No se ha encontrado el evento");
        }
    }

    @Nested
    @DisplayName("listarTodo")
    class ListarTodo {

        @Test
        @DisplayName("debería retornar la lista completa de eventos")
        void deberiaRetornarListaDeEventos() {
            Eventos otroEvento = new Eventos();
            otroEvento.setId(2L);
            otroEvento.setNombre("Concierto");

            when(eventosRepository.findAll()).thenReturn(List.of(evento, otroEvento));

            List<Eventos> resultado = eventosService.listarTodo();

            assertThat(resultado).hasSize(2).containsExactly(evento, otroEvento);
        }

        @Test
        @DisplayName("debería retornar una lista vacía cuando no hay eventos")
        void deberiaRetornarListaVaciaSiNoHayEventos() {
            when(eventosRepository.findAll()).thenReturn(List.of());

            List<Eventos> resultado = eventosService.listarTodo();

            assertThat(resultado).isEmpty();
        }
    }
}