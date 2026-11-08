-- V3: Preparar a tabela usuarios para criptografia dos nomes

-- Aumentar o tamanho da coluna nome
ALTER TABLE usuarios
    MODIFY COLUMN nome VARCHAR(512) NOT NULL;

-- Identificar quais nomes já foram criptografados
ALTER TABLE usuarios
    ADD COLUMN nome_criptografado BOOLEAN NOT NULL DEFAULT FALSE;
