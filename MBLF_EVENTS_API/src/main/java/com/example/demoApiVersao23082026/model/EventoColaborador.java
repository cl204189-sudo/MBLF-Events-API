package com.example.demoApiVersao23082026.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "evento_colaboradores")
public class EventoColaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 100)
    private String funcao;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDENTE;

    private Boolean podeChat = true;

    private Boolean podeCalendario = true;

    private Boolean podeTarefas = true;

    @Column(name = "pode_ver_inscritos")
    private Boolean podeVerInscritos = false;

    @Column(name = "pode_registrar_presencas")
    private Boolean podeRegistrarPresencas = false;

    @Column(name = "pode_ver_relatorios")
    private Boolean podeVerRelatorios = false;

    @Column(name = "pode_silenciar")
    private Boolean podeSilenciar = false;

    @Column(name = "pode_expulsar")
    private Boolean podeExpulsar = false;

    @Column(name = "pode_liberar_certificados")
    private Boolean podeLiberarCertificados = false;

    private Boolean silenciado = false;

    @Column(name = "silenciado_ate")
    private LocalDateTime silenciadoAte;

    @Column(
            name = "data_convite",
            insertable = false,
            updatable = false
    )
    private LocalDateTime dataConvite;

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    public enum Status {
        PENDENTE,
        ACEITO,
        RECUSADO,
        EXPULSO
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getPodeChat() {
        return podeChat;
    }

    public void setPodeChat(Boolean podeChat) {
        this.podeChat = podeChat;
    }

    public Boolean getPodeCalendario() {
        return podeCalendario;
    }

    public void setPodeCalendario(Boolean podeCalendario) {
        this.podeCalendario = podeCalendario;
    }

    public Boolean getPodeTarefas() {
        return podeTarefas;
    }

    public void setPodeTarefas(Boolean podeTarefas) {
        this.podeTarefas = podeTarefas;
    }

    public Boolean getPodeVerInscritos() {
        return podeVerInscritos;
    }

    public void setPodeVerInscritos(Boolean podeVerInscritos) {
        this.podeVerInscritos = podeVerInscritos;
    }

    public Boolean getPodeRegistrarPresencas() {
        return podeRegistrarPresencas;
    }

    public void setPodeRegistrarPresencas(Boolean podeRegistrarPresencas) {
        this.podeRegistrarPresencas = podeRegistrarPresencas;
    }

    public Boolean getPodeVerRelatorios() {
        return podeVerRelatorios;
    }

    public void setPodeVerRelatorios(Boolean podeVerRelatorios) {
        this.podeVerRelatorios = podeVerRelatorios;
    }

    public Boolean getPodeSilenciar() {
        return podeSilenciar;
    }

    public void setPodeSilenciar(Boolean podeSilenciar) {
        this.podeSilenciar = podeSilenciar;
    }

    public Boolean getPodeExpulsar() {
        return podeExpulsar;
    }

    public void setPodeExpulsar(Boolean podeExpulsar) {
        this.podeExpulsar = podeExpulsar;
    }

    public Boolean getPodeLiberarCertificados() {
        return podeLiberarCertificados;
    }

    public void setPodeLiberarCertificados(Boolean podeLiberarCertificados) {
        this.podeLiberarCertificados = podeLiberarCertificados;
    }

    public Boolean getSilenciado() {
        return silenciado;
    }

    public void setSilenciado(Boolean silenciado) {
        this.silenciado = silenciado;
    }

    public LocalDateTime getSilenciadoAte() {
        return silenciadoAte;
    }

    public void setSilenciadoAte(LocalDateTime silenciadoAte) {
        this.silenciadoAte = silenciadoAte;
    }

    public LocalDateTime getDataConvite() {
        return dataConvite;
    }

    public void setDataConvite(LocalDateTime dataConvite) {
        this.dataConvite = dataConvite;
    }

    public LocalDateTime getDataResposta() {
        return dataResposta;
    }

    public void setDataResposta(LocalDateTime dataResposta) {
        this.dataResposta = dataResposta;
    }
}