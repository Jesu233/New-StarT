
CREATE TABLE reservas(
    id          BIGINT        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre      VARCHAR (255) NOT NULL,
    fechaInicio DATETIME NOT NULL,
    fechaFin    DATETIME NOT NULL,
    tipo        VARCHAR (255) NOT NULL,
    cliente     VARCHAR (255) NOT NULL,
    estado      VARCHAR (255) NOT NULL



    )