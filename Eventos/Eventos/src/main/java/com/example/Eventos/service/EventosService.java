package com.example.Eventos.service;


import com.example.Eventos.model.Eventos;
import com.example.Eventos.repository.EventosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class EventosService {

    @Autowired
    private EventosRepository eventosRepository;


    @Transactional
    public Eventos agregarEvento(Eventos eventos) {
        //aquí están todos los datos del model excepto el Id

        return eventosRepository.save(eventos);

    }

    @Transactional
    public void eliminarEvento(Long id) {
        Eventos evento = eventosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el evento"));
        eventosRepository.delete(evento);

    }

    @Transactional
    public Eventos actualizarEvento(Long id, Eventos eventos) {
        return eventosRepository.findById(id).map(eventos1 ->  {
            eventos1.setNombre(eventos.getNombre());
            eventos1.setCapacidad(eventos.getCapacidad());
            eventos1.setFechaEvento(eventos.getFechaEvento());
            eventos1.setLugarEvento(eventos.getLugarEvento());
            return eventosRepository.save(eventos1);
        }).orElseThrow(() -> new RuntimeException("No se ha encontrado el evento"));

    }


    public Eventos buscarPorId(Long id) {
        return eventosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el evento"));

    }

    public Eventos buscarPorNombre(String nombre) {
        return (eventosRepository.findByNombre(nombre).orElseThrow(() -> new RuntimeException("No se ha encontrado el evento")));
    }

    public List<Eventos> listarTodo() {
        return eventosRepository.findAll();
    }




}
