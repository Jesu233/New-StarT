package com.example.localidades.service;

import com.example.localidades.model.Estadisticas;
import com.example.localidades.model.Localidades;
import com.example.localidades.repository.LocalidadesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LocalidadesService {

    @Autowired
    private LocalidadesRepository repository;

    public List<Localidades> listar() {
        log.info("Listando todas las localidades");
        return repository.findAll();
    }

    public Optional<Localidades> obtener(Long id) {
        log.info("Buscando localidad con ID {}", id);
        return repository.findById(id);
    }

    public Optional<Localidades> obtenerPorRecintoId(Long recintoId) {
        log.info("Buscando localidad con recintoId {}", recintoId);
        return repository.findByRecintoId(recintoId);
    }

    public List<Localidades> buscarPorNombre(String nombre) {
        log.info("Buscando localidades con nombre: {}", nombre);
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public Localidades guardar(Localidades localidad) {
        log.info("Guardando localidad: {}", localidad.getNombre());
        return repository.save(localidad);
    }

    public Optional<Localidades> actualizar(Long id, Localidades localidad) {
        log.info("Actualizando localidad con ID {}", id);
        return repository.findById(id).map(existente -> {
            existente.setNombre(localidad.getNombre());
            existente.setCampoDelantero(localidad.getCampoDelantero());
            existente.setCampoTrasero(localidad.getCampoTrasero());
            existente.setPlateaBaja(localidad.getPlateaBaja());
            existente.setPlateaAlta(localidad.getPlateaAlta());
            existente.setRecintoId(localidad.getRecintoId());
            return repository.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        log.info("Eliminando localidad con ID {}", id);
        return repository.findById(id).map(localidad -> {
            repository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public Optional<Long> calcularCapacidadTotal(Long id) {
        log.info("Calculando capacidad total para localidad ID {}", id);
        return repository.findById(id).map(localidad ->
                localidad.getCampoDelantero() +
                        localidad.getCampoTrasero() +
                        localidad.getPlateaBaja() +
                        localidad.getPlateaAlta()
        );
    }

    public Estadisticas obtenerEstadisticas() {
        log.info("Obteniendo estadisticas de localidades");
        List<Localidades> localidades = repository.findAll();

        long total = localidades.size();
        long mayor = localidades.stream()
                .mapToLong(l -> l.getCampoDelantero() + l.getCampoTrasero() +
                        l.getPlateaBaja() + l.getPlateaAlta())
                .max().orElse(0);
        long menor = localidades.stream()
                .mapToLong(l -> l.getCampoDelantero() + l.getCampoTrasero() +
                        l.getPlateaBaja() + l.getPlateaAlta())
                .min().orElse(0);
        long promedio = localidades.stream()
                .mapToLong(l -> l.getCampoDelantero() + l.getCampoTrasero() +
                        l.getPlateaBaja() + l.getPlateaAlta())
                .sum() / (total == 0 ? 1 : total);

        return new Estadisticas(total, promedio, mayor, menor);
    }
}