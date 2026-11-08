package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class ModeracaoDTO {

    private Integer id;
    private Integer eventoId;
    private String eventoTitulo;

    private Integer executorId;
    private String executorNome;

    private Integer alvoId;
    private String alvoNome;

    private String acao;
    private String motivo;

    private LocalDateTime dataAcao;

    public ModeracaoDTO(
            Integer id,
            Integer eventoId,
            String eventoTitulo,
            Integer executorId,
            String executorNome,
            Integer alvoId,
            String alvoNome,
            String acao,
            String motivo,
            LocalDateTime dataAcao) {

        this.id = id;
        this.eventoId = eventoId;
        this.eventoTitulo = eventoTitulo;
        this.executorId = executorId;
        this.executorNome = executorNome;
        this.alvoId = alvoId;
        this.alvoNome = alvoNome;
        this.acao = acao;
        this.motivo = motivo;
        this.dataAcao = dataAcao;
    }

    public Integer getId() {
        return id;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public String getEventoTitulo() {
        return eventoTitulo;
    }

    public Integer getExecutorId() {
        return executorId;
    }

    public String getExecutorNome() {
        return executorNome;
    }

    public Integer getAlvoId() {
        return alvoId;
    }

    public String getAlvoNome() {
        return alvoNome;
    }

    public String getAcao() {
        return acao;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDateTime getDataAcao() {
        return dataAcao;
    }
}