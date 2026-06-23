package com.example.promocion.service;

import com.example.promocion.dto.CalculoPromoRequest;
import com.example.promocion.model.Promo;
import com.example.promocion.model.TipoDescuento;
import com.example.promocion.model.UsoPromo;
import com.example.promocion.repository.PromoRepository;
import com.example.promocion.repository.UsoPromoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromoServiceTest {

    @Mock
    private PromoRepository promoRepository;

    @Mock
    private UsoPromoRepository usoRepository;

    @InjectMocks
    private PromoService promoService;

    // ============================================================
    //  TEST 1: OBTENER TODAS LAS PROMOCIONES
    // ============================================================
    @Test
    void deberiaObtenerTodasLasPromociones() {
        // Preparar
        Promo promo1 = Promo.builder()
                .idRegla("B_STDER")
                .descripcion("20% descuento Banco Estado")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .build();

        Promo promo2 = Promo.builder()
                .idRegla("T_ENTEL")
                .descripcion("15% descuento Entel")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(15.0)
                .build();

        List<Promo> promos = Arrays.asList(promo1, promo2);

        when(promoRepository.findAll()).thenReturn(promos);

        // Ejecutar
        List<Promo> resultado = promoService.obtenerTodas();

        // Verificar
        assertEquals(2, resultado.size());
        assertEquals("B_STDER", resultado.get(0).getIdRegla());
        assertEquals("T_ENTEL", resultado.get(1).getIdRegla());
        verify(promoRepository).findAll();
    }

    // ============================================================
    //  TEST 2: OBTENER PROMOCIÓN POR ID EXISTENTE
    // ============================================================
    @Test
    void deberiaObtenerPromocionPorIdCuandoExiste() {
        // Preparar
        Promo promo = Promo.builder()
                .idRegla("B_STDER")
                .descripcion("20% descuento Banco Estado")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .build();

        when(promoRepository.findById("B_STDER")).thenReturn(Optional.of(promo));

        // Ejecutar
        Promo resultado = promoService.obtenerPorId("B_STDER");

        // Verificar
        assertNotNull(resultado);
        assertEquals("B_STDER", resultado.getIdRegla());
        assertEquals("20% descuento Banco Estado", resultado.getDescripcion());
        verify(promoRepository).findById("B_STDER");
    }

    // ============================================================
    //  TEST 3: OBTENER PROMOCIÓN POR ID NO EXISTENTE
    // ============================================================
    @Test
    void deberiaLanzarExcepcionCuandoPromocionNoExiste() {
        // Preparar
        when(promoRepository.findById("INEXISTENTE")).thenReturn(Optional.empty());

        // Ejecutar y verificar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            promoService.obtenerPorId("INEXISTENTE");
        });

        assertEquals("Regla no encontrada con ID: INEXISTENTE", exception.getMessage());
        verify(promoRepository).findById("INEXISTENTE");
    }

    // ============================================================
    //  TEST 4: GUARDAR NUEVA PROMOCIÓN EXITOSA
    // ============================================================
    @Test
    void deberiaGuardarNuevaPromocion() {
        // Preparar
        Promo promo = Promo.builder()
                .idRegla("B_STDER")
                .descripcion("20% descuento Banco Estado")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .build();

        when(promoRepository.existsById("B_STDER")).thenReturn(false);
        when(promoRepository.save(any(Promo.class))).thenReturn(promo);

        // Ejecutar
        Promo resultado = promoService.guardarNuevaPromo(promo);

        // Verificar
        assertNotNull(resultado);
        assertEquals("B_STDER", resultado.getIdRegla());
        verify(promoRepository).existsById("B_STDER");
        verify(promoRepository).save(any(Promo.class));
    }

    // ============================================================
    //  TEST 5: GUARDAR PROMOCIÓN CON ID DUPLICADO
    // ============================================================
    @Test
    void deberiaLanzarExcepcionCuandoIdYaExiste() {
        // Preparar
        Promo promo = Promo.builder()
                .idRegla("B_STDER")
                .descripcion("20% descuento Banco Estado")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .build();

        when(promoRepository.existsById("B_STDER")).thenReturn(true);

        // Ejecutar y verificar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            promoService.guardarNuevaPromo(promo);
        });

        assertEquals("Ya existe una promoción con ID: B_STDER", exception.getMessage());
        verify(promoRepository).existsById("B_STDER");
        verify(promoRepository, never()).save(any(Promo.class));
    }

    // ============================================================
    //  TEST 6: ACTUALIZAR PROMOCIÓN EXITOSA
    // ============================================================
    @Test
    void deberiaActualizarPromocion() {
        // Preparar
        Promo existing = Promo.builder()
                .idRegla("B_STDER")
                .descripcion("20% descuento Banco Estado")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .stockMaximo(100)
                .build();

        Promo updated = Promo.builder()
                .idRegla("B_STDER")
                .descripcion("25% descuento Banco Estado (actualizado)")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(25.0)
                .stockMaximo(150)
                .build();

        when(promoRepository.findById("B_STDER")).thenReturn(Optional.of(existing));
        when(promoRepository.save(any(Promo.class))).thenReturn(updated);

        // Ejecutar
        Promo resultado = promoService.actualizarPromo("B_STDER", updated);

        // Verificar
        assertNotNull(resultado);
        assertEquals("25% descuento Banco Estado (actualizado)", resultado.getDescripcion());
        assertEquals(25.0, resultado.getValorDescuento());
        assertEquals(150, resultado.getStockMaximo());
        verify(promoRepository).findById("B_STDER");
        verify(promoRepository).save(any(Promo.class));
    }

    // ============================================================
    //  TEST 7: ACTUALIZAR PROMOCIÓN NO EXISTENTE
    // ============================================================
    @Test
    void deberiaLanzarExcepcionAlActualizarPromocionNoExistente() {
        // Preparar
        Promo promo = Promo.builder()
                .idRegla("INEXISTENTE")
                .descripcion("Descripción")
                .build();

        when(promoRepository.findById("INEXISTENTE")).thenReturn(Optional.empty());

        // Ejecutar y verificar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            promoService.actualizarPromo("INEXISTENTE", promo);
        });

        assertEquals("Regla no encontrada con ID: INEXISTENTE", exception.getMessage());
        verify(promoRepository).findById("INEXISTENTE");
        verify(promoRepository, never()).save(any(Promo.class));
    }

    // ============================================================
    //  TEST 8: ELIMINAR PROMOCIÓN EXITOSA
    // ============================================================
    @Test
    void deberiaEliminarPromocion() {
        // Preparar
        when(promoRepository.existsById("B_STDER")).thenReturn(true);
        doNothing().when(promoRepository).deleteById("B_STDER");

        // Ejecutar
        promoService.eliminarPromo("B_STDER");

        // Verificar
        verify(promoRepository).existsById("B_STDER");
        verify(promoRepository).deleteById("B_STDER");
    }

    // ============================================================
    //  TEST 9: ELIMINAR PROMOCIÓN NO EXISTENTE
    // ============================================================
    @Test
    void deberiaLanzarExcepcionAlEliminarPromocionNoExistente() {
        // Preparar
        when(promoRepository.existsById("INEXISTENTE")).thenReturn(false);

        // Ejecutar y verificar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            promoService.eliminarPromo("INEXISTENTE");
        });

        assertEquals("Regla no encontrada con ID: INEXISTENTE", exception.getMessage());
        verify(promoRepository).existsById("INEXISTENTE");
        verify(promoRepository, never()).deleteById(anyString());
    }

    // ============================================================
    //  TEST 10: CALCULAR DESCUENTO EXITOSO (PORCENTAJE)
    // ============================================================
    @Test
    void deberiaCalcularDescuentoPorcentaje() {
        // Preparar
        Promo promo = Promo.builder()
                .idRegla("B_STDER")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .limiteEntradasCliente(5)
                .build();

        CalculoPromoRequest request = new CalculoPromoRequest();
        request.setIdRegla("B_STDER");
        request.setIdEvento(1001L);
        request.setUserRun("12345678-9");
        request.setCantidadTickets(4);
        request.setMontoTotal(80000.0);

        when(promoRepository.findById("B_STDER")).thenReturn(Optional.of(promo));
        when(usoRepository.sumTicketsByRunAndEvento("12345678-9", 1001L)).thenReturn(0);

        // Ejecutar
        Double resultado = promoService.calcularDescuentoFinal(request);

        // Verificar: 80000 - (20% de 80000) = 64000
        assertEquals(64000.0, resultado);
        verify(promoRepository).findById("B_STDER");
        verify(usoRepository).sumTicketsByRunAndEvento("12345678-9", 1001L);
    }

    // ============================================================
    //  TEST 11: CALCULAR DESCUENTO - USUARIO CON CUPO AGOTADO
    // ============================================================
    @Test
    void deberiaRetornarMontoTotalCuandoUsuarioAgotoCupo() {
        // Preparar
        Promo promo = Promo.builder()
                .idRegla("B_STDER")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .limiteEntradasCliente(5)
                .build();

        CalculoPromoRequest request = new CalculoPromoRequest();
        request.setIdRegla("B_STDER");
        request.setIdEvento(1001L);
        request.setUserRun("12345678-9");
        request.setCantidadTickets(4);
        request.setMontoTotal(80000.0);

        when(promoRepository.findById("B_STDER")).thenReturn(Optional.of(promo));
        when(usoRepository.sumTicketsByRunAndEvento("12345678-9", 1001L)).thenReturn(5);

        // Ejecutar
        Double resultado = promoService.calcularDescuentoFinal(request);

        // Verificar: el usuario ya usó 5 entradas, su cupo está agotado
        assertEquals(80000.0, resultado);
        verify(promoRepository).findById("B_STDER");
        verify(usoRepository).sumTicketsByRunAndEvento("12345678-9", 1001L);
    }

    // ============================================================
    //  TEST 12: CALCULAR DESCUENTO - DESCUENTO PARCIAL
    // ============================================================
    @Test
    void deberiaCalcularDescuentoParcialCuandoCupoEsLimitado() {
        // Preparar
        Promo promo = Promo.builder()
                .idRegla("B_STDER")
                .tipo(TipoDescuento.PORCENTAJE)
                .valorDescuento(20.0)
                .limiteEntradasCliente(5)
                .build();

        CalculoPromoRequest request = new CalculoPromoRequest();
        request.setIdRegla("B_STDER");
        request.setIdEvento(1001L);
        request.setUserRun("12345678-9");
        request.setCantidadTickets(4);
        request.setMontoTotal(80000.0);

        when(promoRepository.findById("B_STDER")).thenReturn(Optional.of(promo));
        when(usoRepository.sumTicketsByRunAndEvento("12345678-9", 1001L)).thenReturn(3);

        // Ejecutar
        Double resultado = promoService.calcularDescuentoFinal(request);

        // Verificar: usuario ya usó 3, solo le quedan 2 cupos
        // 80000 - (20% de 80000 * 2/4) = 80000 - (16000 * 0.5) = 72000
        assertEquals(72000.0, resultado);
        verify(promoRepository).findById("B_STDER");
        verify(usoRepository).sumTicketsByRunAndEvento("12345678-9", 1001L);
    }
}