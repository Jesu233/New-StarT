package com.example.Reservas.service;

import com.example.Reservas.model.Reservas;
import com.example.Reservas.repository.ReservasRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ReservasService {

    @Autowired
    private ReservasRepository reservasRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;


    @Transactional
    public Reservas guardarReserva(Reservas reservas) {
        return reservasRepository.save(reservas);
    }

    public void eliminarReserva(Long id) {
        if (!reservasRepository.existsById(id)) {
            throw new RuntimeException("No se ha encontrado la reserva.");
        }
        reservasRepository.deleteById(id);
    }


    public Reservas actualizarReserva(Long id, Reservas reservas) {
        if (!reservasRepository.existsById(id)) {
            throw new RuntimeException("No se ha encontrado la reserva");
        }
        reservas.setId(id);
        return reservasRepository.save(reservas);
    }

    public Reservas buscarPorId(Long id) {
        if (!reservasRepository.existsById(id)) {
            throw new RuntimeException("No se ha encontrado la reserva");
        }
        return reservasRepository.findById(id).get();
    }

    public Reservas buscarPorNombre(String nombre) {
        return reservasRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la reserva"));
    }


    public boolean verificarCupoEnEventos(Long eventoId) {
        Integer capacidadDisponible = webClientBuilder.build()
                .get()
                .uri("http://localhost:8083/api/eventos/" + eventoId + "/capacidad")
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        return capacidadDisponible != null && capacidadDisponible > 0;
    }
}