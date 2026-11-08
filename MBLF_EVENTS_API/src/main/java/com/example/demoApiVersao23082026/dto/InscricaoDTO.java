package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class InscricaoDTO {

    private Integer id;

    private Integer usuarioId;
    private String usuarioNome;

    private Integer eventoId;
    private String eventoTitulo;

    private LocalDateTime dataInscricao;

    private String status;

    private String tokenQr;

    private LocalDateTime dataCancelamento;

    public InscricaoDTO(
            Integer id,
            Integer usuarioId,
            String usuarioNome,
            Integer eventoId,
            String eventoTitulo,
            LocalDateTime dataInscricao,
            String status,
            String tokenQr,
            LocalDateTime dataCancelamento) {


        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.eventoId = eventoId;
        this.eventoTitulo = eventoTitulo;
        this.dataInscricao = dataInscricao;
        this.status = status;
        this.tokenQr = tokenQr;
        this.dataCancelamento = dataCancelamento;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public String getEventoTitulo() {
        return eventoTitulo;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public String getStatus() {
        return status;
    }

    public String getTokenQr() {
        return tokenQr;
    }

    public LocalDateTime getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(LocalDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }
}