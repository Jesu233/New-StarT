CREATE TABLE uso_promociones (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_regla VARCHAR(20) NOT NULL,
    id_evento BIGINT NOT NULL,
    -- RUN del usuario (Vínculo con MS-Auth)
    user_run VARCHAR(20) NOT NULL,
    -- Cuántos tickets usaron el descuento en esta transacción
    cantidad_tickets INT NOT NULL,
    -- ID de la reserva (Para trazabilidad con MS-Reservas)
    reserva_id VARCHAR(100) NOT NULL,
    -- Fecha exacta del registro
    fecha_uso DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Índices para que el Service consulte ultra rápido
    INDEX idx_usuario_evento (user_run, id_evento),
    -- Llave foránea (Opcional, para mantener integridad con la Tabla A)
    CONSTRAINT fk_promo_uso FOREIGN KEY (id_regla) REFERENCES promociones(id_regla)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;