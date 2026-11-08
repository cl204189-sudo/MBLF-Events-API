package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class NotificacaoDTO {

    private Integer id;
    private Integer eventoId;
    private String tipo;
    private String titulo;
    private String mensagem;
    private Boolean lida;
    private LocalDateTime dataCriacao;

    public NotificacaoDTO(
            Integer id,
            Integer eventoId,
            String tipo,
            String titulo,
            String mensagem,
            Boolean lida,
            LocalDateTime dataCriacao) {

        this.id = id;
        this.eventoId = eventoId;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.lida = lida;
        this.dataCriacao = dataCriacao;
    }

    public Integer getId() {
        return id;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Boolean getLida() {
        return lida;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}