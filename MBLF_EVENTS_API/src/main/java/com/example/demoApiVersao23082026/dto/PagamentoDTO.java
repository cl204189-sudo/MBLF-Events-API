package com.example.demoApiVersao23082026.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoDTO {

    private Integer id;

    private Integer usuarioId;
    private String usuarioNome;

    private Integer eventoId;
    private String eventoTitulo;

    private Integer planoId;
    private String planoNome;
    private String tipoPlano;

    private BigDecimal valorPago;
    private String status;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAprovacao;

    public PagamentoDTO() {
    }

    public PagamentoDTO(
            Integer id,
            Integer usuarioId,
            String usuarioNome,
            Integer eventoId,
            String eventoTitulo,
            Integer planoId,
            String planoNome,
            String tipoPlano,
            BigDecimal valorPago,
            String status,
            LocalDateTime dataCriacao,
            LocalDateTime dataAprovacao) {

        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.eventoId = eventoId;
        this.eventoTitulo = eventoTitulo;
        this.planoId = planoId;
        this.planoNome = planoNome;
        this.tipoPlano = tipoPlano;
        this.valorPago = valorPago;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAprovacao = dataAprovacao;
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

    public Integer getPlanoId() {
        return planoId;
    }

    public String getPlanoNome() {
        return planoNome;
    }

    public String getTipoPlano() {
        return tipoPlano;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }
}