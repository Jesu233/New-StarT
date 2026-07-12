CREATE TABLE eventos (
    id              BIGINT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre          VARCHAR (255)   NOT NULL,
    capacidad       INT             NOT NULL,
    fecha_evento    DATETIME   NOT NULL,
    lugar_evento    VARCHAR (255)   NOT NULL
)
