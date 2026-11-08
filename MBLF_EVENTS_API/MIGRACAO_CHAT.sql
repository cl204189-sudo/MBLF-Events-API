-- Execute no MySQL antes de iniciar a API atualizada.
-- Mensagens antigas permanecem privadas da organização.
ALTER TABLE mensagens ADD COLUMN canal VARCHAR(20) NOT NULL DEFAULT 'ORGANIZACAO';
CREATE INDEX idx_mensagens_evento_canal ON mensagens (evento_id, canal);
