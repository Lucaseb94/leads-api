CREATE TABLE leads (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    endereço VARCHAR(255),
    regiao VARCHAR(255),
    area_interesse VARCHAR(20) NOT NULL,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
