package com.example.Tickets.service;


import com.example.Tickets.model.Tickets;
import com.example.Tickets.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<Tickets> findAll() {
        return ticketRepository.findAll();
    }

    public Optional<Tickets> findById(Long id) {
        return ticketRepository.findById(id);
    }
    public Tickets save(Tickets ticket) {
        return ticketRepository.save(ticket);
    }

    public void delete(Tickets ticket) {
        ticketRepository.delete(ticket);
    }

    public void deleteById  (Long id) {
        ticketRepository.deleteById(id);
    }
}
