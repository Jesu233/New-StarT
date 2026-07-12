package com.example.Tickets.service;

import com.example.Tickets.model.Tickets;
import com.example.Tickets.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<Tickets> findAll() {
        log.info("Obteniendo todos los tickets");
        return ticketRepository.findAll();
    }

    public Optional<Tickets> findById(Long idTicket) {
        log.info("Buscando ticket con ID: {}", idTicket);
        return ticketRepository.findById(idTicket);
    }


    public List<Tickets> findByEvento(Long idEvento) {
        log.info("Buscando tickets del evento: {}", idEvento);
        return ticketRepository.findByIdEvento(idEvento);
    }

    // ✅ Ahora usa camelCase
    public List<Tickets> findByTipo(String tipoTicket) {
        log.info("Buscando tickets por tipo: {}", tipoTicket);
        return ticketRepository.findByTipoTicket(tipoTicket);
    }

    public List<Tickets> findByPrecioLessThan(int precio) {
        log.info("Buscando tickets con precio menor a: {}", precio);
        return ticketRepository.findByPrecioLessThan(precio);
    }

    public List<Tickets> findByStockGreaterThan(int stock) {
        log.info("Buscando tickets con stock mayor a: {}", stock);
        return ticketRepository.findByStockGreaterThan(stock);
    }

    public Tickets save(Tickets ticket) {
        log.info("Guardando nuevo ticket: {}", ticket.getNombre());
        if (ticket.getFechaGenerado() == null) {
            ticket.setFechaGenerado(LocalDateTime.now());
        }
        return ticketRepository.save(ticket);
    }

    public Tickets update(Tickets ticket) {
        log.info("Actualizando ticket con ID: {}", ticket.getIdTicket());
        if (!ticketRepository.existsById(ticket.getIdTicket())) {
            throw new RuntimeException("Ticket no encontrado con ID: " + ticket.getIdTicket());
        }
        return ticketRepository.save(ticket);
    }

    public void delete(Tickets ticket) {
        log.info("Eliminando ticket con ID: {}", ticket.getIdTicket());
        ticketRepository.delete(ticket);
    }

    public void deleteById(Long idTicket) {
        log.info("Eliminando ticket con ID: {}", idTicket);
        ticketRepository.deleteById(idTicket);
    }
}