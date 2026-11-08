package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class PresencaDTO {

    private Integer id;

    private Integer inscricaoId;

    private Integer usuarioId;
    private String usuarioNome;

    private Integer eventoId;
    private String eventoTitulo;

    private LocalDateTime dataLeitura;

    public PresencaDTO(
            Integer id,
            Integer inscricaoId,
            Integer usuarioId,
            String usuarioNome,
            Integer eventoId,
            String eventoTitulo,
            LocalDateTime dataLeitura) {

        this.id = id;
        this.inscricaoId = inscricaoId;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.eventoId = eventoId;
        this.eventoTitulo = eventoTitulo;
        this.dataLeitura = dataLeitura;
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

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public String getEventoTitulo() {
        return eventoTitulo;
    }

    public LocalDateTime getDataLeitura() {
        return dataLeitura;
    }
}