package com.example.Tickets.repository;

import com.example.Tickets.model.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Tickets,Long > {
}
