package com.example.demoApiVersao23082026.dto;

public class ModeracaoRequestDTO {

    private Integer alvoId;
    private String motivo;

    public ModeracaoRequestDTO() {
    }

    public Integer getAlvoId() {
        return alvoId;
    }

    public void setAlvoId(Integer alvoId) {
        this.alvoId = alvoId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}