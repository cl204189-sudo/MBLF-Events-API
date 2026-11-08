package com.example.demoApiVersao23082026.dto;

public class FuncionarioPermissaoDTO {

    private Boolean visualizarUsuarios;
    private Boolean bloquearUsuarios;
    private Boolean visualizarEventos;
    private Boolean moderarEventos;
    private Boolean visualizarPagamentos;
    private Boolean gerenciarPlanos;

    public Boolean getVisualizarUsuarios() {
        return visualizarUsuarios;
    }

    public void setVisualizarUsuarios(Boolean visualizarUsuarios) {
        this.visualizarUsuarios = visualizarUsuarios;
    }

    public Boolean getBloquearUsuarios() {
        return bloquearUsuarios;
    }

    public void setBloquearUsuarios(Boolean bloquearUsuarios) {
        this.bloquearUsuarios = bloquearUsuarios;
    }

    public Boolean getVisualizarEventos() {
        return visualizarEventos;
    }

    public void setVisualizarEventos(Boolean visualizarEventos) {
        this.visualizarEventos = visualizarEventos;
    }

    public Boolean getModerarEventos() {
        return moderarEventos;
    }

    public void setModerarEventos(Boolean moderarEventos) {
        this.moderarEventos = moderarEventos;
    }

    public Boolean getVisualizarPagamentos() {
        return visualizarPagamentos;
    }

    public void setVisualizarPagamentos(Boolean visualizarPagamentos) {
        this.visualizarPagamentos = visualizarPagamentos;
    }

    public Boolean getGerenciarPlanos() {
        return gerenciarPlanos;
    }

    public void setGerenciarPlanos(Boolean gerenciarPlanos) {
        this.gerenciarPlanos = gerenciarPlanos;
    }
}