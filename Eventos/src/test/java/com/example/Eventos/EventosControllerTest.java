package com.example.Eventos.controller;

import com.example.Eventos.assembler.EventosModelAssembler;
import com.example.Eventos.model.Eventos;
import com.example.Eventos.service.EventosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("EventosController - pruebas unitarias")
class EventosControllerTest {

    @Mock
    private EventosService eventosService;

    @Mock
    private EventosModelAssembler assembler;

    @InjectMocks
    private EventosController eventosController;

    private Eventos evento;

    @BeforeEach
    void setUp() {
        evento = new Eventos();
        evento.setId(1L);
        evento.setNombre("Cosmódromo");
        evento.setCapacidad(4000L);
        evento.setFechaEvento(LocalDateTime.of(2026, 8, 15, 20, 0, 0));
        evento.setLugarEvento("Estadio Nacional");

        lenient().when(assembler.toModel(any(Eventos.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Nested
    @DisplayName("listarTodo")
    class ListarTodo {

        @Test
        @DisplayName("debería retornar 200 con la lista de eventos enlazados")
        void deberiaListarTodosLosEventos() {
            Eventos otroEvento = new Eventos();
            otroEvento.setId(2L);
            otroEvento.setNombre("Concierto");

            when(eventosService.listarTodo()).thenReturn(List.of(evento, otroEvento));

            ResponseEntity<List<Eventos>> respuesta = eventosController.listarTodo();

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(respuesta.getBody()).containsExactly(evento, otroEvento);
            verify(assembler, times(2)).toModel(any(Eventos.class));
        }

        @Test
        @DisplayName("debería retornar 200 con lista vacía cuando no hay eventos")
        void deberiaRetornarListaVaciaCuandoNoHayEventos() {
            when(eventosService.listarTodo()).thenReturn(List.of());

            ResponseEntity<List<Eventos>> respuesta = eventosController.listarTodo();

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(respuesta.getBody()).isEmpty();
            verifyNoInteractions(assembler);
        }
    }

    @Nested
    @DisplayName("obtenerCapacidad")
    class ObtenerCapacidad {

        @Test
        @DisplayName("debería retornar la capacidad del evento solicitado")
        void deberiaRetornarCapacidad() {
            when(eventosService.buscarPorId(1L)).thenReturn(evento);

            ResponseEntity<Long> respuesta = eventosController.obtenerCapacidad(1L);

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(respuesta.getBody()).isEqualTo(4000L);
        }
    }

    @Nested
    @DisplayName("agregarEvento")
    class AgregarEvento {

        @Test
        @DisplayName("debería crear el evento y retornar 201")
        void deberiaCrearEvento() {
            when(eventosService.agregarEvento(evento)).thenReturn(evento);

            ResponseEntity<Eventos> respuesta = eventosController.agregarEvento(evento);

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(respuesta.getBody()).isEqualTo(evento);
            verify(assembler).toModel(evento);
        }
    }

    @Nested
    @DisplayName("eliminarEvento")
    class EliminarEvento {

        @Test
        @DisplayName("debería eliminar el evento y retornar 204")
        void deberiaEliminarEvento() {
            ResponseEntity<Void> respuesta = eventosController.eliminarEvento(1L);

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(respuesta.getBody()).isNull();
            verify(eventosService).eliminarEvento(1L);
        }
    }

    @Nested
    @DisplayName("actualizarEvento")
    class ActualizarEvento {

        @Test
        @DisplayName("debería actualizar el evento y retornar 200")
        void deberiaActualizarEvento() {
            Eventos datosActualizados = new Eventos();
            datosActualizados.setNombre("Festival Actualizado");

            when(eventosService.actualizarEvento(1L, datosActualizados)).thenReturn(evento);

            ResponseEntity<Eventos> respuesta = eventosController.actualizarEvento(1L, datosActualizados);

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(respuesta.getBody()).isEqualTo(evento);
            verify(assembler).toModel(evento);
        }
    }

    @Nested
    @DisplayName("buscarPorId")
    class BuscarPorId {

        @Test
        @DisplayName("debería retornar el evento encontrado con 200")
        void deberiaRetornarEventoPorId() {
            when(eventosService.buscarPorId(1L)).thenReturn(evento);

            ResponseEntity<Eventos> respuesta = eventosController.buscarPorId(1L);

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(respuesta.getBody()).isEqualTo(evento);
            verify(assembler).toModel(evento);
        }
    }

    @Nested
    @DisplayName("buscarPorNombre")
    class BuscarPorNombre {

        @Test
        @DisplayName("debería retornar el evento encontrado con 200")
        void deberiaRetornarEventoPorNombre() {
            when(eventosService.buscarPorNombre("Cosmódromo")).thenReturn(evento);

            ResponseEntity<Eventos> respuesta = eventosController.buscarPorNombre("Cosmódromo");

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(respuesta.getBody()).isEqualTo(evento);
            verify(assembler).toModel(evento);
        }
    }
}