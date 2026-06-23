package com.example.promocion.service;

import com.example.promocion.dto.CalculoPromoRequest;
import com.example.promocion.dto.RegistroUsoDTO;
import com.example.promocion.model.Promo;
import com.example.promocion.model.TipoDescuento;
import com.example.promocion.model.UsoPromo;
import com.example.promocion.repository.PromoRepository;
import com.example.promocion.repository.UsoPromoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PromoService {

    private final PromoRepository promoRepository;
    private final UsoPromoRepository usoRepository;

    // ========== OBTENER TODAS ==========
    public List<Promo> obtenerTodas() {
        log.info("Obteniendo todas las promociones");
        return promoRepository.findAll();
    }

    // ========== OBTENER POR ID ==========
    public Promo obtenerPorId(String idRegla) {
        log.info("Obteniendo promoción con ID: {}", idRegla);
        return promoRepository.findById(idRegla)
                .orElseThrow(() -> {
                    log.warn("Regla no encontrada: {}", idRegla);
                    return new RuntimeException("Regla no encontrada con ID: " + idRegla);
                });
    }

    // ========== GUARDAR NUEVA ==========
    public Promo guardarNuevaPromo(Promo promo) {
        log.info("Guardando nueva promoción con ID: {}", promo.getIdRegla());

        // Validar que no exista una promoción con el mismo ID
        if (promoRepository.existsById(promo.getIdRegla())) {
            log.warn("Ya existe una promoción con ID: {}", promo.getIdRegla());
            throw new RuntimeException("Ya existe una promoción con ID: " + promo.getIdRegla());
        }

        return promoRepository.save(promo);
    }

    // ========== ACTUALIZAR ==========
    public Promo actualizarPromo(String idRegla, Promo promo) {
        log.info("Actualizando promoción con ID: {}", idRegla);

        // Verificar que existe
        Promo existing = promoRepository.findById(idRegla)
                .orElseThrow(() -> {
                    log.warn("Regla no encontrada para actualizar: {}", idRegla);
                    return new RuntimeException("Regla no encontrada con ID: " + idRegla);
                });

        // Actualizar solo los campos que vienen en el request
        if (promo.getDescripcion() != null) {
            existing.setDescripcion(promo.getDescripcion());
        }
        if (promo.getTipo() != null) {
            existing.setTipo(promo.getTipo());
        }
        if (promo.getValorDescuento() != null) {
            existing.setValorDescuento(promo.getValorDescuento());
        }
        if (promo.getFechaInicio() != null) {
            existing.setFechaInicio(promo.getFechaInicio());
        }
        if (promo.getFechaFin() != null) {
            existing.setFechaFin(promo.getFechaFin());
        }
        if (promo.getBancoRequerido() != null) {
            existing.setBancoRequerido(promo.getBancoRequerido());
        }
        if (promo.getCompaniaRequerida() != null) {
            existing.setCompaniaRequerida(promo.getCompaniaRequerida());
        }
        if (promo.getMinTicketsReq() != null) {
            existing.setMinTicketsReq(promo.getMinTicketsReq());
        }
        if (promo.getStockMaximo() != null) {
            existing.setStockMaximo(promo.getStockMaximo());
        }
        if (promo.getLimiteEntradasCliente() != null) {
            existing.setLimiteEntradasCliente(promo.getLimiteEntradasCliente());
        }
        if (promo.getMontoMinimoRequerido() != null) {
            existing.setMontoMinimoRequerido(promo.getMontoMinimoRequerido());
        }

        Promo updated = promoRepository.save(existing);
        log.info("Promoción actualizada exitosamente: {}", updated.getIdRegla());
        return updated;
    }

    // ========== ELIMINAR ==========
    public void eliminarPromo(String idRegla) {
        log.info("Eliminando promoción con ID: {}", idRegla);

        if (!promoRepository.existsById(idRegla)) {
            log.warn("Regla no encontrada para eliminar: {}", idRegla);
            throw new RuntimeException("Regla no encontrada con ID: " + idRegla);
        }

        promoRepository.deleteById(idRegla);
        log.info("Promoción eliminada exitosamente: {}", idRegla);
    }

    // ========== CALCULAR DESCUENTO ==========
    public Double calcularDescuentoFinal(CalculoPromoRequest request) {
        log.info("Calculando descuento para usuario: {}, evento: {}, regla: {}",
                request.getUserRun(), request.getIdEvento(), request.getIdRegla());

        // 1. Obtener la regla de la Tabla A
        Promo promo = promoRepository.findById(request.getIdRegla())
                .orElseThrow(() -> {
                    log.warn("Regla no encontrada: {}", request.getIdRegla());
                    return new RuntimeException("Regla no encontrada con ID: " + request.getIdRegla());
                });

        // 2. Validar que la promoción esté vigente
        // (Puedes agregar validación de fechas aquí)

        // 3. Consultar historial en Tabla B
        Integer usadosDB = usoRepository.sumTicketsByRunAndEvento(
                request.getUserRun(),
                request.getIdEvento()
        );
        int yaComprados = (usadosDB != null) ? usadosDB : 0;
        log.debug("Usuario {} ya compró {} tickets para el evento {}",
                request.getUserRun(), yaComprados, request.getIdEvento());

        // 4. Determinar cupos disponibles
        int limiteConfigurado = promo.getLimiteEntradasCliente();
        int cuposDisponibles = limiteConfigurado - yaComprados;

        // Si ya agotó sus entradas con descuento
        if (cuposDisponibles <= 0) {
            log.warn("Usuario {} agotó su cupo para la regla {}",
                    request.getUserRun(), request.getIdRegla());
            return request.getMontoTotal();
        }

        // 5. Calcular beneficio para las entradas que quepan en su cupo
        int cantidadConDescuento = Math.min(request.getCantidadTickets(), cuposDisponibles);

        Double resultado = ejecutarCalculoMatematico(promo, request, cantidadConDescuento);
        log.info("Monto final calculado: {} para usuario {}", resultado, request.getUserRun());
        return resultado;
    }

    private Double ejecutarCalculoMatematico(Promo promo, CalculoPromoRequest req, int cantConDcto) {
        double precioUnitario = req.getMontoTotal() / req.getCantidadTickets();
        double descuentoTotal = 0.0;

        if (promo.getTipo() == TipoDescuento.PORCENTAJE) {
            double ahorroPorTicket = precioUnitario * (promo.getValorDescuento() / 100);
            descuentoTotal = ahorroPorTicket * cantConDcto;
        } else {
            // Monto fijo (se aplica una vez o por ticket según tu regla)
            descuentoTotal = promo.getValorDescuento();
        }

        return req.getMontoTotal() - descuentoTotal;
    }

    // ========== REGISTRAR USO ==========
    @org.springframework.transaction.annotation.Transactional
    public void registrarUsoExitoso(RegistroUsoDTO dto) {
        log.info("Recibida confirmación de pago. Registrando uso en Tabla B para Reserva: {}, Usuario: {}",
                dto.getReservaId(), dto.getUserRun());

        // 1. CONTROL DE SEGURIDAD: Evita registrar la misma compra dos veces
        boolean yaExiste = usoRepository.existsByReservaId(dto.getReservaId());
        if (yaExiste) {
            log.warn("La reserva {} ya estaba registrada en el historial. Ignorando petición duplicada.",
                    dto.getReservaId());
            return;
        }

        // 2. Construir objeto para la Tabla B
        UsoPromo nuevoUso = new UsoPromo();
        nuevoUso.setIdRegla(dto.getIdRegla());
        nuevoUso.setIdEvento(dto.getIdEvento());
        nuevoUso.setUserRun(dto.getUserRun());
        nuevoUso.setCantidadTickets(dto.getCantidadTickets());
        nuevoUso.setReservaId(dto.getReservaId());

        // 3. Guardar en la Base de Datos
        usoRepository.save(nuevoUso);
        log.info("Historial de uso guardado con éxito para la reserva {}", dto.getReservaId());
    }
}