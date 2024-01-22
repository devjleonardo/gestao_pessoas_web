CREATE TABLE pessoa (
    id bigint NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT pk_pessoa PRIMARY KEY (id),
    CONSTRAINT uk_pessoa_email UNIQUE (email)
);

CREATE TABLE pessoa_fisica (
    pessoa_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    CONSTRAINT pk_pessoa_fisica PRIMARY KEY (pessoa_id),
    CONSTRAINT fk_pessoa_fisica_pessoa_id FOREIGN KEY (pessoa_id) 
        REFERENCES pessoa(id) ON DELETE CASCADE,
    CONSTRAINT uk_pessoa_fisica_cpf UNIQUE (cpf)
);

CREATE TABLE pessoa_juridica (
    pessoa_id BIGINT NOT NULL,
    razao_social VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    CONSTRAINT pk_pessoa_juridica PRIMARY KEY (pessoa_id),
    CONSTRAINT fk_pessoa_juridica_pessoa_id FOREIGN KEY (pessoa_id) 
        REFERENCES pessoa(id) ON DELETE CASCADE,
    CONSTRAINT uk_pessoa_juridica_cnpj UNIQUE (cnpj)
);

CREATE TABLE carga_inicial_estado (
    id bigint NOT NULL,
    carga_inicial_executada BOOLEAN NOT NULL,
    CONSTRAINT pk_carga_inicial_estado PRIMARY KEY (id)
);

CREATE TABLE endereco (
    id bigint NOT NULL,
    cep VARCHAR(255) NOT NULL,
    logradouro VARCHAR(255) NOT NULL,
    bairro VARCHAR(255) NOT NULL,
    numero VARCHAR(255) NOT NULL,
    complemento VARCHAR(255),
    cidade VARCHAR(255) NOT NULL,
    uf VARCHAR(255) NOT NULL,
    pessoa_id bigint,
    CONSTRAINT pk_endereco PRIMARY KEY (id),
    CONSTRAINT fk_endereco_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa (id)
);

CREATE SEQUENCE seq_pessoa START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE seq_carga_inicial_estado START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE seq_endereco START WITH 1 INCREMENT BY 1;

ALTER TABLE pessoa ALTER COLUMN id SET DEFAULT nextval('seq_pessoa');

ALTER TABLE carga_inicial_estado ALTER COLUMN id SET DEFAULT nextval('seq_carga_inicial_estado');

ALTER TABLE endereco ALTER COLUMN id SET DEFAULT nextval('seq_endereco');