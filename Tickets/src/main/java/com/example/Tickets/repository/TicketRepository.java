package com.example.Tickets.repository;

import com.example.Tickets.model.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Tickets, Long> {

    // ✅ Ahora usa camelCase
    List<Tickets> findByIdEvento(Long idEvento);
    List<Tickets> findByTipoTicket(String tipo);
    List<Tickets> findByPrecioLessThan(int precio);
    List<Tickets> findByStockGreaterThan(int stock);
}