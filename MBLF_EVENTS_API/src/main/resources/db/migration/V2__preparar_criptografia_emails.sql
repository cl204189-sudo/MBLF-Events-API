ALTER TABLE usuarios
    MODIFY COLUMN email VARCHAR(512) NOT NULL;

ALTER TABLE usuarios
    ADD COLUMN email_hash CHAR(64) NULL,
    ADD COLUMN email_criptografado BOOLEAN NOT NULL DEFAULT FALSE;

CREATE UNIQUE INDEX uk_usuarios_email_hash
    ON usuarios (email_hash);
