package com.example.promocion.repository;

import com.example.promocion.model.UsoPromo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsoPromoRepository extends JpaRepository<UsoPromo, Long> {

    @Query("SELECT SUM(u.cantidadTickets) " + "FROM UsoPromo u " + "WHERE u.userRun = :run AND u.idEvento = :idEvento")
    Integer sumTicketsByRunAndEvento(@Param("run") String run, @Param("idEvento") Long idEvento);

    boolean existsByReservaId(String reservaId);
}
