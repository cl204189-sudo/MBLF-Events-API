package com.example.demoApiVersao23082026.dto;

public class PresencaRequestDTO {

    private String token;
    private Integer eventoId;

    public PresencaRequestDTO() {
    }

    public Integer getEventoId() { return eventoId; }
    public void setEventoId(Integer eventoId) { this.eventoId = eventoId; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}