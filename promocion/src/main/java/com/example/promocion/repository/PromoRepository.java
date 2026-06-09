package com.example.promocion.repository;

import com.example.promocion.model.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PromoRepository extends JpaRepository<Promo, String> {

    @Query("SELECT p FROM Promo p WHERE p.idEvento = :idEvento " +
            "AND :ahora BETWEEN p.fechaInicio AND p.fechaFin " +
            "AND p.usosActuales < p.stockMaximo")
    List<Promo> findActivePromosByEvento(
            @Param("idEvento") Long idEvento,
            @Param("ahora") LocalDateTime ahora
    );
}
