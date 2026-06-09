CREATE TABLE promociones (
    PRIMARY KEY (id_regla),
    id_regla                        VARCHAR(20)                         NOT NULL,
    id_evento                       BIGINT                              NOT NULL,
    descripcion                     VARCHAR(255)                        NOT NULL,
    tipo                            ENUM('PORCENTAJE', 'MONTO_FIJO')    NOT NULL,
    valor_descuento                 DECIMAL(12, 2)                      NOT NULL    DEFAULT 20.00,
    fecha_inicio                    DATETIME                            NOT NULL,
    fecha_fin                       DATETIME                            NOT NULL,
    banco_requerido                 VARCHAR(50)                                     DEFAULT NULL,
    compania_requerida              VARCHAR(50)                                     DEFAULT NULL,
    min_tickets_req                 INT                                             DEFAULT NULL,
    stock_maximo                    INT                                 NOT NULL,
    usos_actuales                   INT                                             DEFAULT 0,
    limite_entradas_por_cliente     INT                                 NOT NULL    DEFAULT 2,
    monto_minimo_requerido          DECIMAL(12, 2)                                  DEFAULT 0.00,
    INDEX idx_evento (id_evento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;