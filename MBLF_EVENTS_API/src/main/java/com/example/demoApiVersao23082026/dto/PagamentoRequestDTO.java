package com.example.demoApiVersao23082026.dto;

public class PagamentoRequestDTO {

    private Integer eventoId;
    private Integer planoId;

    public PagamentoRequestDTO() {
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public void setEventoId(Integer eventoId) {
        this.eventoId = eventoId;
    }

    public Integer getPlanoId() {
        return planoId;
    }

    public void setPlanoId(Integer planoId) {
        this.planoId = planoId;
    }
}