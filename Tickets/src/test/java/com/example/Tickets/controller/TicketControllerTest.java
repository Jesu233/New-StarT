package com.example.Tickets.controller;

import com.example.Tickets.model.Tickets;
import com.example.Tickets.security.jwt.JwtService;
import com.example.Tickets.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

// ✅ Imports de Mockito (los mismos que funcionan en otros proyectos)
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void deberiaObtenerTodosLosTickets() throws Exception {
        Tickets ticket1 = new Tickets();
        ticket1.setIdTicket(1L);
        ticket1.setIdEvento(10L);
        ticket1.setNombre("Entrada VIP");
        ticket1.setDescripcion("Acceso VIP");
        ticket1.setPrecio(15000);
        ticket1.setTipoTicket("VIP");
        ticket1.setStock(100);
        ticket1.setFechaGenerado(LocalDateTime.now());

        Tickets ticket2 = new Tickets();
        ticket2.setIdTicket(2L);
        ticket2.setIdEvento(10L);
        ticket2.setNombre("Entrada General");
        ticket2.setDescripcion("Acceso General");
        ticket2.setPrecio(5000);
        ticket2.setTipoTicket("GENERAL");
        ticket2.setStock(200);
        ticket2.setFechaGenerado(LocalDateTime.now());

        List<Tickets> tickets = Arrays.asList(ticket1, ticket2);

        when(ticketService.findAll()).thenReturn(tickets);

        mockMvc.perform(get("/api/v1/tickets")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketsList[0].idTicket").value(1))
                .andExpect(jsonPath("$._embedded.ticketsList[0].nombre").value("Entrada VIP"))
                .andExpect(jsonPath("$._embedded.ticketsList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.ticketsList[1].idTicket").value(2))
                .andExpect(jsonPath("$._embedded.ticketsList[1].nombre").value("Entrada General"))
                .andExpect(jsonPath("$._embedded.ticketsList[1]._links.self.href").exists());

        verify(ticketService).findAll();
    }

    @Test
    void deberiaObtenerTicketPorId() throws Exception {
        Tickets ticket = new Tickets();
        ticket.setIdTicket(1L);
        ticket.setIdEvento(10L);
        ticket.setNombre("Entrada VIP");
        ticket.setDescripcion("Acceso VIP");
        ticket.setPrecio(15000);
        ticket.setTipoTicket("VIP");
        ticket.setStock(100);
        ticket.setFechaGenerado(LocalDateTime.now());

        when(ticketService.findById(1L)).thenReturn(Optional.of(ticket));

        mockMvc.perform(get("/api/v1/tickets/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTicket").value(1))
                .andExpect(jsonPath("$.nombre").value("Entrada VIP"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists());

        verify(ticketService).findById(1L);
    }

    @Test
    void deberiaRetornar404CuandoTicketNoExiste() throws Exception {
        when(ticketService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/tickets/999")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound());

        verify(ticketService).findById(999L);
    }

    @Test
    void deberiaObtenerTicketsPorEvento() throws Exception {
        Tickets ticket1 = new Tickets();
        ticket1.setIdTicket(1L);
        ticket1.setIdEvento(10L);
        ticket1.setNombre("Entrada VIP");
        ticket1.setDescripcion("Acceso VIP");
        ticket1.setPrecio(15000);
        ticket1.setTipoTicket("VIP");
        ticket1.setStock(100);
        ticket1.setFechaGenerado(LocalDateTime.now());

        Tickets ticket2 = new Tickets();
        ticket2.setIdTicket(2L);
        ticket2.setIdEvento(10L);
        ticket2.setNombre("Entrada General");
        ticket2.setDescripcion("Acceso General");
        ticket2.setPrecio(5000);
        ticket2.setTipoTicket("GENERAL");
        ticket2.setStock(200);
        ticket2.setFechaGenerado(LocalDateTime.now());

        List<Tickets> tickets = Arrays.asList(ticket1, ticket2);

        when(ticketService.findByEvento(10L)).thenReturn(tickets);

        mockMvc.perform(get("/api/v1/tickets/evento/10")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketsList[0].idTicket").value(1))
                .andExpect(jsonPath("$._embedded.ticketsList[0].nombre").value("Entrada VIP"))
                .andExpect(jsonPath("$._embedded.ticketsList[1].idTicket").value(2))
                .andExpect(jsonPath("$._embedded.ticketsList[1].nombre").value("Entrada General"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists())
                .andExpect(jsonPath("$._links.crear.href").exists());

        verify(ticketService).findByEvento(10L);
    }

    @Test
    void deberiaObtenerTicketsPorTipo() throws Exception {
        Tickets ticket1 = new Tickets();
        ticket1.setIdTicket(1L);
        ticket1.setIdEvento(10L);
        ticket1.setNombre("Entrada VIP");
        ticket1.setDescripcion("Acceso VIP");
        ticket1.setPrecio(15000);
        ticket1.setTipoTicket("VIP");
        ticket1.setStock(100);
        ticket1.setFechaGenerado(LocalDateTime.now());

        Tickets ticket2 = new Tickets();
        ticket2.setIdTicket(3L);
        ticket2.setIdEvento(20L);
        ticket2.setNombre("Entrada VIP Concierto");
        ticket2.setDescripcion("Acceso VIP Concierto");
        ticket2.setPrecio(20000);
        ticket2.setTipoTicket("VIP");
        ticket2.setStock(50);
        ticket2.setFechaGenerado(LocalDateTime.now());

        List<Tickets> tickets = Arrays.asList(ticket1, ticket2);

        when(ticketService.findByTipo("VIP")).thenReturn(tickets);

        mockMvc.perform(get("/api/v1/tickets/tipo/VIP")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketsList[0].idTicket").value(1))
                .andExpect(jsonPath("$._embedded.ticketsList[0].tipoTicket").value("VIP"))
                .andExpect(jsonPath("$._embedded.ticketsList[1].idTicket").value(3))
                .andExpect(jsonPath("$._embedded.ticketsList[1].tipoTicket").value("VIP"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists())
                .andExpect(jsonPath("$._links.crear.href").exists());

        verify(ticketService).findByTipo("VIP");
    }

    @Test
    void deberiaObtenerTicketsPorPrecioMenor() throws Exception {
        Tickets ticket1 = new Tickets();
        ticket1.setIdTicket(2L);
        ticket1.setIdEvento(10L);
        ticket1.setNombre("Entrada General");
        ticket1.setDescripcion("Acceso General");
        ticket1.setPrecio(5000);
        ticket1.setTipoTicket("GENERAL");
        ticket1.setStock(200);
        ticket1.setFechaGenerado(LocalDateTime.now());

        Tickets ticket2 = new Tickets();
        ticket2.setIdTicket(4L);
        ticket2.setIdEvento(10L);
        ticket2.setNombre("Entrada Estudiante");
        ticket2.setDescripcion("Acceso Estudiante");
        ticket2.setPrecio(3000);
        ticket2.setTipoTicket("ESTUDIANTE");
        ticket2.setStock(50);
        ticket2.setFechaGenerado(LocalDateTime.now());

        List<Tickets> tickets = Arrays.asList(ticket1, ticket2);

        when(ticketService.findByPrecioLessThan(10000)).thenReturn(tickets);

        mockMvc.perform(get("/api/v1/tickets/precio/menor/10000")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketsList[0].precio").value(5000))
                .andExpect(jsonPath("$._embedded.ticketsList[1].precio").value(3000))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists());

        verify(ticketService).findByPrecioLessThan(10000);
    }

    @Test
    void deberiaObtenerTicketsPorStockMayor() throws Exception {
        Tickets ticket1 = new Tickets();
        ticket1.setIdTicket(1L);
        ticket1.setIdEvento(10L);
        ticket1.setNombre("Entrada VIP");
        ticket1.setDescripcion("Acceso VIP");
        ticket1.setPrecio(15000);
        ticket1.setTipoTicket("VIP");
        ticket1.setStock(100);
        ticket1.setFechaGenerado(LocalDateTime.now());

        Tickets ticket2 = new Tickets();
        ticket2.setIdTicket(2L);
        ticket2.setIdEvento(10L);
        ticket2.setNombre("Entrada General");
        ticket2.setDescripcion("Acceso General");
        ticket2.setPrecio(5000);
        ticket2.setTipoTicket("GENERAL");
        ticket2.setStock(200);
        ticket2.setFechaGenerado(LocalDateTime.now());

        List<Tickets> tickets = Arrays.asList(ticket1, ticket2);

        when(ticketService.findByStockGreaterThan(50)).thenReturn(tickets);

        mockMvc.perform(get("/api/v1/tickets/stock/mayor/50")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketsList[0].stock").value(100))
                .andExpect(jsonPath("$._embedded.ticketsList[1].stock").value(200))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists());

        verify(ticketService).findByStockGreaterThan(50);
    }

    @Test
    void deberiaCrearTicket() throws Exception {
        Tickets ticket = new Tickets();
        ticket.setIdTicket(1L);
        ticket.setIdEvento(10L);
        ticket.setNombre("Entrada VIP");
        ticket.setDescripcion("Acceso VIP");
        ticket.setPrecio(15000);
        ticket.setTipoTicket("VIP");
        ticket.setStock(100);
        ticket.setFechaGenerado(LocalDateTime.now());

        when(ticketService.save(any(Tickets.class))).thenReturn(ticket);

        String json = """
                {
                    "idEvento": 10,
                    "nombre": "Entrada VIP",
                    "descripcion": "Acceso VIP",
                    "precio": 15000,
                    "tipoTicket": "VIP",
                    "stock": 100,
                    "fechaGenerado": "2026-06-23T10:00:00"
                }
                """;

        mockMvc.perform(post("/api/v1/tickets")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTicket").value(1))
                .andExpect(jsonPath("$.nombre").value("Entrada VIP"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists());

        verify(ticketService).save(any(Tickets.class));
    }

    @Test
    void deberiaRetornar400CuandoFaltanCamposObligatorios() throws Exception {
        String json = """
                {
                    "idEvento": 10,
                    "precio": 15000
                }
                """;

        mockMvc.perform(post("/api/v1/tickets")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaActualizarTicket() throws Exception {
        Tickets ticket = new Tickets();
        ticket.setIdTicket(1L);
        ticket.setIdEvento(10L);
        ticket.setNombre("Entrada VIP Actualizada");
        ticket.setDescripcion("Acceso VIP Mejorado");
        ticket.setPrecio(20000);
        ticket.setTipoTicket("VIP");
        ticket.setStock(80);
        ticket.setFechaGenerado(LocalDateTime.now());

        when(ticketService.update(any(Tickets.class))).thenReturn(ticket);

        String json = """
                {
                    "idTicket": 1,
                    "idEvento": 10,
                    "nombre": "Entrada VIP Actualizada",
                    "descripcion": "Acceso VIP Mejorado",
                    "precio": 20000,
                    "tipoTicket": "VIP",
                    "stock": 80,
                    "fechaGenerado": "2026-06-23T10:00:00"
                }
                """;

        mockMvc.perform(put("/api/v1/tickets")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTicket").value(1))
                .andExpect(jsonPath("$.nombre").value("Entrada VIP Actualizada"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(ticketService).update(any(Tickets.class));
    }

    @Test
    void deberiaEliminarTicket() throws Exception {
        doNothing().when(ticketService).delete(any(Tickets.class));

        String json = """
                {
                    "idTicket": 1
                }
                """;

        mockMvc.perform(delete("/api/v1/tickets")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"));

        verify(ticketService).delete(any(Tickets.class));
    }

    @Test
    void deberiaEliminarTicketPorId() throws Exception {
        doNothing().when(ticketService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/tickets/1"))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"));

        verify(ticketService).deleteById(1L);
    }
}