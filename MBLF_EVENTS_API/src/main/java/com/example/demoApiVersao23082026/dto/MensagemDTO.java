package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class MensagemDTO {

    private Integer id;

    private Integer eventoId;

    private Integer usuarioId;
    private String usuarioNome;

    private String mensagem;

    private LocalDateTime dataEnvio;

    public MensagemDTO(
            Integer id,
            Integer eventoId,
            Integer usuarioId,
            String usuarioNome,
            String mensagem,
            LocalDateTime dataEnvio) {

        this.id = id;
        this.eventoId = eventoId;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.mensagem = mensagem;
        this.dataEnvio = dataEnvio;
    }

    public Integer getId() {
        return id;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }
}