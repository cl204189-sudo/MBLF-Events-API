package com.example.demoApiVersao23082026.dto;

import com.example.demoApiVersao23082026.model.TarefaEvento;

import java.time.LocalDateTime;

public class TarefaEventoRequestDTO {

    private Integer responsavelId;

    private String titulo;
    private String descricao;

    private TarefaEvento.Prioridade prioridade;

    private LocalDateTime prazo;

    public TarefaEventoRequestDTO() {
    }

    public Integer getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Integer responsavelId) {
        this.responsavelId = responsavelId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TarefaEvento.Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(TarefaEvento.Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDateTime prazo) {
        this.prazo = prazo;
    }
}