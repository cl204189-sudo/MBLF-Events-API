package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class EventoDTO {

    private Integer id;

    private Integer organizadorId;
    private String organizadorNome;

    private String titulo;
    private String descricao;
    private String tipo;
    private String endereco;
    private String cidade;

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    private Integer cargaHoraria;

    private Integer vagasTotal;
    private Integer vagasDisponiveis;

    private LocalDateTime hubAte;
    private LocalDateTime publicacaoAte;

    private Boolean inscricoesAbertas;

    private String status;
    private String imagemUrl;
    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public EventoDTO() {
    }

    public EventoDTO(
            Integer id,
            Integer organizadorId,
            String organizadorNome,
            String titulo,
            String descricao,
            String tipo,
            String endereco,
            String cidade,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Integer cargaHoraria,
            Integer vagasTotal,
            Integer vagasDisponiveis,
            LocalDateTime hubAte,
            LocalDateTime publicacaoAte,
            Boolean inscricoesAbertas,
            String status) {

        this.id = id;
        this.organizadorId = organizadorId;
        this.organizadorNome = organizadorNome;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.endereco = endereco;
        this.cidade = cidade;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.cargaHoraria = cargaHoraria;
        this.vagasTotal = vagasTotal;
        this.vagasDisponiveis = vagasDisponiveis;
        this.hubAte = hubAte;
        this.publicacaoAte = publicacaoAte;
        this.inscricoesAbertas = inscricoesAbertas;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrganizadorId() {
        return organizadorId;
    }

    public void setOrganizadorId(Integer organizadorId) {
        this.organizadorId = organizadorId;
    }

    public String getOrganizadorNome() {
        return organizadorNome;
    }

    public void setOrganizadorNome(String organizadorNome) {
        this.organizadorNome = organizadorNome;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
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

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Integer getVagasTotal() {
        return vagasTotal;
    }

    public void setVagasTotal(Integer vagasTotal) {
        this.vagasTotal = vagasTotal;
    }

    public Integer getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public void setVagasDisponiveis(Integer vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public LocalDateTime getHubAte() {
        return hubAte;
    }

    public void setHubAte(LocalDateTime hubAte) {
        this.hubAte = hubAte;
    }

    public LocalDateTime getPublicacaoAte() {
        return publicacaoAte;
    }

    public void setPublicacaoAte(LocalDateTime publicacaoAte) {
        this.publicacaoAte = publicacaoAte;
    }

    public Boolean getInscricoesAbertas() {
        return inscricoesAbertas;
    }

    public void setInscricoesAbertas(Boolean inscricoesAbertas) {
        this.inscricoesAbertas = inscricoesAbertas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}