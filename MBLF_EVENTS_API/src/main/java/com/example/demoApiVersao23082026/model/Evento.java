package com.example.demoApiVersao23082026.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "organizador_id", nullable = false)
    private Usuario organizador;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(length = 100)
    private String tipo;

    @Column(length = 255)
    private String endereco;

    @Column(length = 100)
    private String cidade;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "carga_horaria")
    private Integer cargaHoraria;

    @Column(name = "vagas_total")
    private Integer vagasTotal;

    @Column(name = "vagas_disponiveis")
    private Integer vagasDisponiveis;

    @Column(name = "hub_ate")
    private LocalDateTime hubAte;

    @Column(name = "publicacao_ate")
    private LocalDateTime publicacaoAte;

    @Column(name = "inscricoes_abertas")
    private Boolean inscricoesAbertas = false;

    @Column(name = "chat_somente_organizador")
    private Boolean chatSomenteOrganizador = false;

    @Enumerated(EnumType.STRING)
    private Status status = Status.RASCUNHO;

    @Column(name = "imagem_nome", length = 100)
    private String imagemNome;
    public String getImagemNome() { return imagemNome; }
    public void setImagemNome(String imagemNome) { this.imagemNome = imagemNome; }

    @Column(name = "motivo_remocao", columnDefinition = "TEXT")
    private String motivoRemocao;

    @Column(name = "data_criacao", insertable = false, updatable = false)
    private LocalDateTime dataCriacao;

    public enum Status {
        RASCUNHO,
        PUBLICADO,
        ENCERRADO,
        CANCELADO,
        REMOVIDO_ADMIN
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Usuario organizador) {
        this.organizador = organizador;
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

    public Boolean getChatSomenteOrganizador() {
        return chatSomenteOrganizador;
    }

    public void setChatSomenteOrganizador(Boolean chatSomenteOrganizador) {
        this.chatSomenteOrganizador = chatSomenteOrganizador;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMotivoRemocao() {
        return motivoRemocao;
    }

    public void setMotivoRemocao(String motivoRemocao) {
        this.motivoRemocao = motivoRemocao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}