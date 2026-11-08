package com.example.demoApiVersao23082026.dto;

public class PermissaoColaboradorDTO {

    private Boolean podeChat;
    private Boolean podeCalendario;
    private Boolean podeTarefas;

    private Boolean podeVerInscritos;
    private Boolean podeRegistrarPresencas;
    private Boolean podeVerRelatorios;

    private Boolean podeSilenciar;
    private Boolean podeExpulsar;

    private Boolean podeLiberarCertificados;

    public PermissaoColaboradorDTO() {
    }

    public Boolean getPodeChat() {
        return podeChat;
    }

    public void setPodeChat(Boolean podeChat) {
        this.podeChat = podeChat;
    }

    public Boolean getPodeCalendario() {
        return podeCalendario;
    }

    public void setPodeCalendario(Boolean podeCalendario) {
        this.podeCalendario = podeCalendario;
    }

    public Boolean getPodeTarefas() {
        return podeTarefas;
    }

    public void setPodeTarefas(Boolean podeTarefas) {
        this.podeTarefas = podeTarefas;
    }

    public Boolean getPodeVerInscritos() {
        return podeVerInscritos;
    }

    public void setPodeVerInscritos(Boolean podeVerInscritos) {
        this.podeVerInscritos = podeVerInscritos;
    }

    public Boolean getPodeRegistrarPresencas() {
        return podeRegistrarPresencas;
    }

    public void setPodeRegistrarPresencas(Boolean podeRegistrarPresencas) {
        this.podeRegistrarPresencas = podeRegistrarPresencas;
    }

    public Boolean getPodeVerRelatorios() {
        return podeVerRelatorios;
    }

    public void setPodeVerRelatorios(Boolean podeVerRelatorios) {
        this.podeVerRelatorios = podeVerRelatorios;
    }

    public Boolean getPodeSilenciar() {
        return podeSilenciar;
    }

    public void setPodeSilenciar(Boolean podeSilenciar) {
        this.podeSilenciar = podeSilenciar;
    }

    public Boolean getPodeExpulsar() {
        return podeExpulsar;
    }

    public void setPodeExpulsar(Boolean podeExpulsar) {
        this.podeExpulsar = podeExpulsar;
    }

    public Boolean getPodeLiberarCertificados() {
        return podeLiberarCertificados;
    }

    public void setPodeLiberarCertificados(Boolean podeLiberarCertificados) {
        this.podeLiberarCertificados = podeLiberarCertificados;
    }
}