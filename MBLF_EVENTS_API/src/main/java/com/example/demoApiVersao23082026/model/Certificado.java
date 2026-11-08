package com.example.demoApiVersao23082026.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificados")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "inscricao_id", nullable = false, unique = true)
    private Inscricao inscricao;

    @Column(nullable = false, unique = true, length = 100)
    private String codigo;

    @Column(name = "titulo_evento", nullable = false, length = 200)
    private String tituloEvento;

    @Column(name = "nome_participante", nullable = false, length = 150)
    private String nomeParticipante;

    @Column(name = "carga_horaria")
    private Integer cargaHoraria;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDENTE;

    @Column(
            name = "data_geracao",
            insertable = false,
            updatable = false
    )
    private LocalDateTime dataGeracao;

    @Column(name = "data_liberacao")
    private LocalDateTime dataLiberacao;

    @ManyToOne
    @JoinColumn(name = "liberado_por")
    private Usuario liberadoPor;

    public enum Status {
        PENDENTE,
        LIBERADO
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTituloEvento() {
        return tituloEvento;
    }

    public void setTituloEvento(String tituloEvento) {
        this.tituloEvento = tituloEvento;
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public void setNomeParticipante(String nomeParticipante) {
        this.nomeParticipante = nomeParticipante;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public LocalDateTime getDataLiberacao() {
        return dataLiberacao;
    }

    public void setDataLiberacao(LocalDateTime dataLiberacao) {
        this.dataLiberacao = dataLiberacao;
    }

    public Usuario getLiberadoPor() {
        return liberadoPor;
    }

    public void setLiberadoPor(Usuario liberadoPor) {
        this.liberadoPor = liberadoPor;
    }
}