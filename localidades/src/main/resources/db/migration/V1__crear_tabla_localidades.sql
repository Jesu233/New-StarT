CREATE TABLE localidades (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    campo_delantero INT,
    campo_trasero INT,
    platea_baja INT,
    platea_alta INT,
    recinto_id BIGINT
);