package com.example.Tickets.service;

import com.example.Tickets.model.Tickets;
import com.example.Tickets.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    // ============================================================
    //  TEST 1: OBTENER TODOS LOS TICKETS
    // ============================================================
    @Test
    void deberiaObtenerTodosLosTickets() {
        Tickets ticket1 = new Tickets();
        ticket1.setId_ticket(1L);
        ticket1.setNombre("Entrada VIP");
        ticket1.setPrecio(15000);

        Tickets ticket2 = new Tickets();
        ticket2.setId_ticket(2L);
        ticket2.setNombre("Entrada General");
        ticket2.setPrecio(5000);

        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket1, ticket2));

        List<Tickets> tickets = ticketService.findAll();

        assertEquals(2, tickets.size());
        assertEquals("Entrada VIP", tickets.get(0).getNombre());
        assertEquals("Entrada General", tickets.get(1).getNombre());
        verify(ticketRepository).findAll();
    }

    // ============================================================
    //  TEST 2: ENCONTRAR TICKET POR ID EXISTENTE
    // ============================================================
    @Test
    void deberiaEncontrarTicketPorId() {
        Tickets ticket = new Tickets();
        ticket.setId_ticket(1L);
        ticket.setNombre("Entrada VIP");
        ticket.setPrecio(15000);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Optional<Tickets> resultado = ticketService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Entrada VIP", resultado.get().getNombre());
        verify(ticketRepository).findById(1L);
    }

    // ============================================================
    //  TEST 3: ENCONTRAR TICKET POR ID NO EXISTENTE
    // ============================================================
    @Test
    void deberiaRetornarOptionalVacioCuandoTicketNoExiste() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Tickets> resultado = ticketService.findById(999L);

        assertTrue(resultado.isEmpty());
        verify(ticketRepository).findById(999L);
    }

    // ============================================================
    //  TEST 4: GUARDAR TICKET
    // ============================================================
    @Test
    void deberiaGuardarTicket() {
        Tickets ticket = new Tickets();
        ticket.setId_ticket(1L);
        ticket.setNombre("Entrada VIP");
        ticket.setPrecio(15000);
        ticket.setFecha_generado(null);

        Tickets ticketGuardado = new Tickets();
        ticketGuardado.setId_ticket(1L);
        ticketGuardado.setNombre("Entrada VIP");
        ticketGuardado.setPrecio(15000);
        ticketGuardado.setFecha_generado(LocalDateTime.now());

        when(ticketRepository.save(any(Tickets.class))).thenReturn(ticketGuardado);

        Tickets resultado = ticketService.save(ticket);

        assertNotNull(resultado);
        assertNotNull(resultado.getFecha_generado());
        assertEquals("Entrada VIP", resultado.getNombre());
        verify(ticketRepository).save(any(Tickets.class));
    }

    // ============================================================
    //  TEST 5: ACTUALIZAR TICKET EXISTENTE
    // ============================================================
    @Test
    void deberiaActualizarTicket() {
        Tickets ticket = new Tickets();
        ticket.setId_ticket(1L);
        ticket.setNombre("Entrada VIP Actualizada");
        ticket.setPrecio(20000);

        when(ticketRepository.existsById(1L)).thenReturn(true);
        when(ticketRepository.save(any(Tickets.class))).thenReturn(ticket);

        Tickets resultado = ticketService.update(ticket);

        assertNotNull(resultado);
        assertEquals("Entrada VIP Actualizada", resultado.getNombre());
        assertEquals(20000, resultado.getPrecio());
        verify(ticketRepository).existsById(1L);
        verify(ticketRepository).save(any(Tickets.class));
    }

    // ============================================================
    //  TEST 6: ACTUALIZAR TICKET NO EXISTENTE
    // ============================================================
    @Test
    void deberiaLanzarExcepcionCuandoTicketNoExisteAlActualizar() {
        Tickets ticket = new Tickets();
        ticket.setId_ticket(999L);
        ticket.setNombre("Ticket Inexistente");

        when(ticketRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService.update(ticket);
        });

        assertEquals("Ticket no encontrado con ID: 999", exception.getMessage());
        verify(ticketRepository).existsById(999L);
        verify(ticketRepository, never()).save(any(Tickets.class));
    }

    // ============================================================
    //  TEST 7: ELIMINAR TICKET POR OBJETO
    // ============================================================
    @Test
    void deberiaEliminarTicket() {
        Tickets ticket = new Tickets();
        ticket.setId_ticket(1L);
        ticket.setNombre("Entrada VIP");

        doNothing().when(ticketRepository).delete(ticket);

        ticketService.delete(ticket);

        verify(ticketRepository).delete(ticket);
    }

    // ============================================================
    //  TEST 8: ELIMINAR TICKET POR ID
    // ============================================================
    @Test
    void deberiaEliminarTicketPorId() {
        Long id = 1L;
        doNothing().when(ticketRepository).deleteById(id);

        ticketService.deleteById(id);

        verify(ticketRepository).deleteById(id);
    }

    // ============================================================
    //  TEST 9: BUSCAR TICKETS POR EVENTO
    // ============================================================
    @Test
    void deberiaBuscarTicketsPorEvento() {
        Tickets ticket1 = new Tickets();
        ticket1.setId_ticket(1L);
        ticket1.setId_evento(10L);
        ticket1.setNombre("Entrada VIP");

        Tickets ticket2 = new Tickets();
        ticket2.setId_ticket(2L);
        ticket2.setId_evento(10L);
        ticket2.setNombre("Entrada General");

        // ✅ CAMBIADO: findByIdEvento en lugar de findById_evento
        when(ticketRepository.findByIdEvento(10L)).thenReturn(Arrays.asList(ticket1, ticket2));

        List<Tickets> tickets = ticketService.findByEvento(10L);

        assertEquals(2, tickets.size());
        assertEquals(10L, tickets.get(0).getId_evento());
        assertEquals(10L, tickets.get(1).getId_evento());
        verify(ticketRepository).findByIdEvento(10L);
    }

    // ============================================================
    //  TEST 10: BUSCAR TICKETS POR TIPO
    // ============================================================
    @Test
    void deberiaBuscarTicketsPorTipo() {
        Tickets ticket1 = new Tickets();
        ticket1.setId_ticket(1L);
        ticket1.setTipo_ticket("VIP");
        ticket1.setNombre("Entrada VIP");

        Tickets ticket2 = new Tickets();
        ticket2.setId_ticket(3L);
        ticket2.setTipo_ticket("VIP");
        ticket2.setNombre("Entrada VIP Concierto");

        // ✅ CAMBIADO: findByTipoTicket en lugar de findByTipo_ticket
        when(ticketRepository.findByTipoTicket("VIP")).thenReturn(Arrays.asList(ticket1, ticket2));

        List<Tickets> tickets = ticketService.findByTipo("VIP");

        assertEquals(2, tickets.size());
        assertEquals("VIP", tickets.get(0).getTipo_ticket());
        assertEquals("VIP", tickets.get(1).getTipo_ticket());
        verify(ticketRepository).findByTipoTicket("VIP");
    }

    // ============================================================
    //  TEST 11: BUSCAR TICKETS POR PRECIO MENOR A
    // ============================================================
    @Test
    void deberiaBuscarTicketsPorPrecioMenor() {
        Tickets ticket1 = new Tickets();
        ticket1.setId_ticket(2L);
        ticket1.setPrecio(5000);
        ticket1.setNombre("Entrada General");

        Tickets ticket2 = new Tickets();
        ticket2.setId_ticket(4L);
        ticket2.setPrecio(3000);
        ticket2.setNombre("Entrada Estudiante");

        when(ticketRepository.findByPrecioLessThan(10000)).thenReturn(Arrays.asList(ticket1, ticket2));

        List<Tickets> tickets = ticketService.findByPrecioLessThan(10000);

        assertEquals(2, tickets.size());
        assertTrue(tickets.get(0).getPrecio() < 10000);
        assertTrue(tickets.get(1).getPrecio() < 10000);
        verify(ticketRepository).findByPrecioLessThan(10000);
    }

    // ============================================================
    //  TEST 12: BUSCAR TICKETS POR STOCK MAYOR A
    // ============================================================
    @Test
    void deberiaBuscarTicketsPorStockMayor() {
        Tickets ticket1 = new Tickets();
        ticket1.setId_ticket(1L);
        ticket1.setStock(100);
        ticket1.setNombre("Entrada VIP");

        Tickets ticket2 = new Tickets();
        ticket2.setId_ticket(2L);
        ticket2.setStock(200);
        ticket2.setNombre("Entrada General");

        when(ticketRepository.findByStockGreaterThan(50)).thenReturn(Arrays.asList(ticket1, ticket2));

        List<Tickets> tickets = ticketService.findByStockGreaterThan(50);

        assertEquals(2, tickets.size());
        assertTrue(tickets.get(0).getStock() > 50);
        assertTrue(tickets.get(1).getStock() > 50);
        verify(ticketRepository).findByStockGreaterThan(50);
    }
}