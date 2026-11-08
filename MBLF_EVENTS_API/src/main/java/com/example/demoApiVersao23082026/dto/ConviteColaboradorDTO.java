package com.example.demoApiVersao23082026.dto;

public class ConviteColaboradorDTO {

    private Integer usuarioConvidadoId;
    private String funcao;

    public ConviteColaboradorDTO() {
    }

    public Integer getUsuarioConvidadoId() {
        return usuarioConvidadoId;
    }

    public void setUsuarioConvidadoId(Integer usuarioConvidadoId) {
        this.usuarioConvidadoId = usuarioConvidadoId;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
}