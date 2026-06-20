package com.example.Eventos;

import com.example.Eventos.assembler.EventosModelAssembler;
import com.example.Eventos.controller.EventosController;
import com.example.Eventos.model.Eventos;
import com.example.Eventos.security.jwt.JwtService;
import com.example.Eventos.service.EventosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = EventosController.class,

        excludeAutoConfiguration = {
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,

        }
)
@Import(EventosModelAssembler.class)
@AutoConfigureMockMvc(addFilters = false)

@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=none",
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
@TestPropertySource(properties = "spring.jpa.open-in-view=false")
public class EventosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventosService eventosService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deberiaBuscarPorIdYRetornarHateoas() throws Exception {
        Eventos evento = new Eventos();
        evento.setId(1L);
        evento.setNombre("Cosmódromo");
        evento.setCapacidad(1000L);
        evento.setFechaEvento(LocalDateTime.of(2026, 8, 15, 20, 0, 0));
        evento.setLugarEvento("Magnolias");

        when(eventosService.buscarPorId(1L)).thenReturn(evento);

        mockMvc.perform(get("/api/v1/evento/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Cosmódromo"))
                .andExpect(jsonPath("$.fechaEvento").value("2026-08-15T20:00:00"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists())
                .andExpect(jsonPath("$._links.actualizar.href").exists())
                .andExpect(jsonPath("$._links.eliminar.href").exists());

        verify(eventosService).buscarPorId(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deberiaListarTodoComoArray() throws Exception {
        Eventos evento1 = new Eventos();
        evento1.setId(1L);
        evento1.setNombre("Evento Uno");

        Eventos evento2 = new Eventos();
        evento2.setId(2L);
        evento2.setNombre("Evento Dos");

        when(eventosService.listarTodo()).thenReturn(List.of(evento1, evento2));

        mockMvc.perform(get("/api/v1/evento")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Evento Uno"))
                .andExpect(jsonPath("$[0]._links.self.href").exists())
                .andExpect(jsonPath("$[1].nombre").value("Evento Dos"));

        verify(eventosService).listarTodo();
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void deberiaBuscarPorNombre() throws Exception {
        Eventos evento = new Eventos();
        evento.setId(3L);
        evento.setNombre("Concierto");

        when(eventosService.buscarPorNombre("Concierto")).thenReturn(evento);

        mockMvc.perform(get("/api/v1/evento/nombre/Concierto")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Concierto"));

        verify(eventosService).buscarPorNombre("Concierto");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deberiaRetornar400CuandoFaltanCamposObligatorios() throws Exception {
        String jsonIncompleto = """
                {
                    "lugarEvento": "Cancha 1"
                }
                """;

        mockMvc.perform(post("/api/v1/evento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonIncompleto))
                .andExpect(status().isBadRequest());
    }
}