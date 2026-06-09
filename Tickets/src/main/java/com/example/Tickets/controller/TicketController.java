package com.example.Tickets.controller;

import com.example.Tickets.model.Tickets;
import com.example.Tickets.repository.TicketRepository;
import com.example.Tickets.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Tickets>> findAll(){
        return ResponseEntity.ok(ticketService.findAll());
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Tickets> findById(@PathVariable Long id){
        return ticketService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Tickets> save(@RequestBody @Valid Tickets tickets) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.save(tickets));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Tickets> update(@RequestBody @Valid Tickets tickets){
        return ResponseEntity.ok(ticketService.save(tickets));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity delete(@RequestBody Tickets tickets){
        ticketService.delete(tickets);
        return ResponseEntity.noContent().build();
    }
}

