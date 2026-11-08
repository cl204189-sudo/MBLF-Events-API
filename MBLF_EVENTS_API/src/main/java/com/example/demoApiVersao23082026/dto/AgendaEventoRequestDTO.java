package com.example.demoApiVersao23082026.dto;

import com.example.demoApiVersao23082026.model.AgendaEvento;

import java.time.LocalDateTime;

public class AgendaEventoRequestDTO {

    private String titulo;
    private String descricao;

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    private AgendaEvento.Tipo tipo;

    public AgendaEventoRequestDTO() {
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

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public AgendaEvento.Tipo getTipo() {
        return tipo;
    }

    public void setTipo(AgendaEvento.Tipo tipo) {
        this.tipo = tipo;
    }
}