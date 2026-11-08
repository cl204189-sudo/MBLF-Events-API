package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class AgendaEventoDTO {

    private Integer id;

    private Integer eventoId;

    private Integer criadoPorId;
    private String criadoPorNome;

    private String titulo;
    private String descricao;

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    private String tipo;

    public AgendaEventoDTO(
            Integer id,
            Integer eventoId,
            Integer criadoPorId,
            String criadoPorNome,
            String titulo,
            String descricao,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            String tipo) {

        this.id = id;
        this.eventoId = eventoId;
        this.criadoPorId = criadoPorId;
        this.criadoPorNome = criadoPorNome;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public Integer getCriadoPorId() {
        return criadoPorId;
    }

    public String getCriadoPorNome() {
        return criadoPorNome;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public String getTipo() {
        return tipo;
    }
}