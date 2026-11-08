package com.example.demoApiVersao23082026.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String mensagem;
    private LocalDateTime dataHora;

    public ErrorResponse(String mensagem) {
        this.mensagem = mensagem;
        this.dataHora = LocalDateTime.now();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}