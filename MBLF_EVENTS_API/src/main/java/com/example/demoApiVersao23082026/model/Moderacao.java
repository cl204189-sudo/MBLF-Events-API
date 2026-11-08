package com.example.demoApiVersao23082026.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "moderacoes")
public class Moderacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "executor_id", nullable = false)
    private Usuario executor;

    @ManyToOne
    @JoinColumn(name = "alvo_id")
    private Usuario alvo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Acao acao;

    @Column(length = 255)
    private String motivo;

    @Column(
            name = "data_acao",
            insertable = false,
            updatable = false
    )
    private LocalDateTime dataAcao;

    public enum Acao {
        SILENCIAR,
        DESSILENCIAR,
        EXPULSAR,
        SILENCIAR_TODOS,
        LIBERAR_TODOS,
        DAR_PERMISSAO,
        REMOVER_PERMISSAO
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Usuario getExecutor() {
        return executor;
    }

    public void setExecutor(Usuario executor) {
        this.executor = executor;
    }

    public Usuario getAlvo() {
        return alvo;
    }

    public void setAlvo(Usuario alvo) {
        this.alvo = alvo;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getDataAcao() {
        return dataAcao;
    }

    public void setDataAcao(LocalDateTime dataAcao) {
        this.dataAcao = dataAcao;
    }
}