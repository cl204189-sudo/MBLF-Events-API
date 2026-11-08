package com.example.demoApiVersao23082026.model;

import jakarta.persistence.*;

@Entity
@Table(name = "funcionario_permissoes")
public class FuncionarioPermissao {

    @Id
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "visualizar_usuarios")
    private Boolean visualizarUsuarios = false;

    @Column(name = "bloquear_usuarios")
    private Boolean bloquearUsuarios = false;

    @Column(name = "visualizar_eventos")
    private Boolean visualizarEventos = true;

    @Column(name = "moderar_eventos")
    private Boolean moderarEventos = false;

    @Column(name = "visualizar_pagamentos")
    private Boolean visualizarPagamentos = false;

    @Column(name = "gerenciar_planos")
    private Boolean gerenciarPlanos = false;

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

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