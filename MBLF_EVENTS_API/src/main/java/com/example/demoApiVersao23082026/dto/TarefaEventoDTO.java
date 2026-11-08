package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class TarefaEventoDTO {

    private Integer id;
    private Integer eventoId;

    private Integer criadoPorId;
    private String criadoPorNome;

    private Integer responsavelId;
    private String responsavelNome;

    private String titulo;
    private String descricao;

    private String prioridade;
    private String status;

    private LocalDateTime prazo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    public TarefaEventoDTO(
            Integer id,
            Integer eventoId,
            Integer criadoPorId,
            String criadoPorNome,
            Integer responsavelId,
            String responsavelNome,
            String titulo,
            String descricao,
            String prioridade,
            String status,
            LocalDateTime prazo,
            LocalDateTime dataCriacao,
            LocalDateTime dataConclusao) {

        this.id = id;
        this.eventoId = eventoId;
        this.criadoPorId = criadoPorId;
        this.criadoPorNome = criadoPorNome;
        this.responsavelId = responsavelId;
        this.responsavelNome = responsavelNome;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
        this.prazo = prazo;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
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

    public Integer getResponsavelId() {
        return responsavelId;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getPrazo() {
        return prazo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }
}