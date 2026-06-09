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

@RequiredArgsConstructor
@Service
@Slf4j
public class PromoService {

        private final PromoRepository promoRepository;
        private final UsoPromoRepository usoRepository;

        public Double calcularDescuentoFinal(CalculoPromoRequest request) {
            log.info("Calculando descuento para usuario: {}, evento: {}, regla: {}", request.getUserRun(), request.getIdEvento(), request.getIdRegla());
            // 1. Obtener la regla de la Tabla A
            Promo promo = promoRepository.findById(request.getIdRegla())
                    .orElseThrow(() -> {
                        log.warn("Regla no encontrada: {}", request.getIdRegla());
                        return new RuntimeException("Regla no encontrada");
                    });

            // 2. Consultar historial en Tabla B: ¿Cuántas ha usado ya?
            Integer usadosDB = usoRepository.sumTicketsByRunAndEvento(
                    request.getUserRun(),
                    request.getIdEvento()
            );
            int yaComprados = (usadosDB != null) ? usadosDB : 0;
            log.debug("Usuario {} ya compró {} tickets para el evento {}",
                    request.getUserRun(), yaComprados, request.getIdEvento());

            // 3. Determinar cuántos cupos le quedan (Límite 2 o 4 de la Tabla A)
            int limiteConfigurado = promo.getLimiteEntradasCliente();
            int cuposDisponibles = limiteConfigurado - yaComprados;

            // Si ya agotó sus entradas con descuento, paga total normal
            if (cuposDisponibles <= 0) {
                log.warn("Usuario {} agotó su cupo par la regla {}",
                        request.getUserRun(), request.getIdRegla());
                return request.getMontoTotal();
            }

            // 4. Calcular el beneficio solo para las entradas que quepan en su cupo
            // Ejemplo: Quiere 4, pero solo le quedan 2 cupos. Solo 2 llevan descuento.
            int cantidadConDescuento = Math.min(request.getCantidadTickets(), cuposDisponibles);

            Double resultado = ejecutarCalculoMatematico(promo, request, cantidadConDescuento);
            log.info("Monto final calculado: {} para usuario {}", resultado, request.getUserRun());
            return resultado;
            //return ejecutarCalculoMatematico(promo, request, cantidadConDescuento);
        }

        private Double ejecutarCalculoMatematico(Promo promo, CalculoPromoRequest req, int cantConDcto) {
            double precioUnitario = req.getMontoTotal() / req.getCantidadTickets();
            double descuentoTotal = 0.0;

            if (promo.getTipo() == TipoDescuento.PORCENTAJE) {
                // Ejemplo: 20% de descuento
                double ahorroPorTicket = precioUnitario * (promo.getValorDescuento() / 100);
                descuentoTotal = ahorroPorTicket * cantConDcto;
            } else {
                // Monto fijo (se aplica una vez o por ticket según tu regla)
                descuentoTotal = promo.getValorDescuento();
            }

            return req.getMontoTotal() - descuentoTotal;
        }
        public Promo guardarNuevaPromo(Promo promo) {
            return promoRepository.save(promo);
        }

    @org.springframework.transaction.annotation.Transactional
    public void registrarUsoExitoso(RegistroUsoDTO dto) {
        log.info("Recibida confirmación de pago. Registrando uso en Tabla B para Reserva: {}, Usuario: {}", dto.getReservaId(), dto.getUserRun());

        // 1. CONTROL DE SEGURIDAD: Evita registrar la misma compra dos veces si la red falla
        boolean yaExiste = usoRepository.existsByReservaId(dto.getReservaId());
        if (yaExiste) {
            log.warn("La reserva {} ya estaba registrada en el historial. Ignorando petición duplicada.", dto.getReservaId());
            return;
        }

        // 2. Si es una compra nueva, se construye el objeto para la Tabla B
        UsoPromo nuevoUso = new UsoPromo();
        nuevoUso.setIdRegla(dto.getIdRegla());
        nuevoUso.setIdEvento(dto.getIdEvento());
        nuevoUso.setUserRun(dto.getUserRun());
        nuevoUso.setCantidadTickets(dto.getCantidadTickets());
        nuevoUso.setReservaId(dto.getReservaId());
        // nuevoUso.setFechaUso(LocalDateTime.now()); // Descomenta esta línea si no le pusiste default en el modelo

        // 3. Guardar en la Base de Datos
        usoRepository.save(nuevoUso);
        log.info("Historial de uso guardado con éxito para la reserva {}", dto.getReservaId());
    }
}
