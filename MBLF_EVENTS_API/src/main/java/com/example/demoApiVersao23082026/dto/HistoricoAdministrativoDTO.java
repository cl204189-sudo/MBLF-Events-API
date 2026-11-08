package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class HistoricoAdministrativoDTO {

    private Integer id;

    private Integer usuarioId;
    private String usuarioNome;

    private String acao;
    private String entidade;
    private Integer entidadeId;

    private String descricao;

    private LocalDateTime dataAcao;

    public HistoricoAdministrativoDTO(
            Integer id,
            Integer usuarioId,
            String usuarioNome,
            String acao,
            String entidade,
            Integer entidadeId,
            String descricao,
            LocalDateTime dataAcao) {

        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.acao = acao;
        this.entidade = entidade;
        this.entidadeId = entidadeId;
        this.descricao = descricao;
        this.dataAcao = dataAcao;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public String getAcao() {
        return acao;
    }

    public String getEntidade() {
        return entidade;
    }

    public Integer getEntidadeId() {
        return entidadeId;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataAcao() {
        return dataAcao;
    }
}