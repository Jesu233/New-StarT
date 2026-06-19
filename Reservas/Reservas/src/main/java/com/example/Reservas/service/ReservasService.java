package com.example.Reservas.service;

import com.example.Reservas.model.Eventos;
import com.example.Reservas.model.Reservas;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.Reservas.repository.ReservasRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReservasService {



    @Autowired
    private ReservasRepository reservasRepository;

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
        if (reservasRepository.existsById(id)) {
            reservasRepository.save(reservas);
        }
        throw new RuntimeException("No se ha encontrado la reserva");

    }

    public Reservas buscarPorId(Long id) {
        if (reservasRepository.existsById(id)) {
            return reservasRepository.findById(id).get();
        }
        throw new RuntimeException("No se ha encontrado la reserva");
    }

    public Reservas buscarPorNombre(String nombre) {
        return reservasRepository.findByNombre(nombre).orElseThrow(() -> new RuntimeException("No se ha encontrado la reserva"));
    }

    @Autowired
    private WebClient.Builder webClientBuilder;

    public boolean verificarCupoEnEventos(Long eventoId) {
        // Suponiendo que el microservicio de Eventos corre en el puerto 8083 :)
        Integer capacidadDisponible = webClientBuilder.build()
                .get()
                .uri("http://localhost:8083/api/eventos/" + eventoId + "/capacidad")
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        return capacidadDisponible != null && capacidadDisponible > 0;
    }
}






