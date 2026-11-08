package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class CertificadoDTO {

    private Integer id;

    private Integer inscricaoId;

    private Integer usuarioId;
    private String nomeParticipante;

    private Integer eventoId;
    private String tituloEvento;

    private String codigo;
    private Integer cargaHoraria;

    private String status;

    private LocalDateTime dataGeracao;
    private LocalDateTime dataLiberacao;

    private Integer liberadoPorId;
    private String liberadoPorNome;

    public CertificadoDTO(
            Integer id,
            Integer inscricaoId,
            Integer usuarioId,
            String nomeParticipante,
            Integer eventoId,
            String tituloEvento,
            String codigo,
            Integer cargaHoraria,
            String status,
            LocalDateTime dataGeracao,
            LocalDateTime dataLiberacao,
            Integer liberadoPorId,
            String liberadoPorNome) {

        this.id = id;
        this.inscricaoId = inscricaoId;
        this.usuarioId = usuarioId;
        this.nomeParticipante = nomeParticipante;
        this.eventoId = eventoId;
        this.tituloEvento = tituloEvento;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
        this.status = status;
        this.dataGeracao = dataGeracao;
        this.dataLiberacao = dataLiberacao;
        this.liberadoPorId = liberadoPorId;
        this.liberadoPorNome = liberadoPorNome;
    }

    public Integer getId() {
        return id;
    }

    public Integer getInscricaoId() {
        return inscricaoId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public String getTituloEvento() {
        return tituloEvento;
    }

    public String getCodigo() {
        return codigo;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public LocalDateTime getDataLiberacao() {
        return dataLiberacao;
    }

    public Integer getLiberadoPorId() {
        return liberadoPorId;
    }

    public String getLiberadoPorNome() {
        return liberadoPorNome;
    }
}