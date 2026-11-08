package com.example.demoApiVersao23082026.dto;

public class RelatorioEventoDTO {
    private Integer eventoId;
    private String eventoTitulo;
    private long totalInscricoes;
    private long inscricoesAtivas;
    private long cancelamentos;
    private long presentes;
    private long certificadosLiberados;
    private long moderacoes;

    public RelatorioEventoDTO(Integer eventoId, String eventoTitulo, long totalInscricoes,
                              long inscricoesAtivas, long cancelamentos, long presentes,
                              long certificadosLiberados, long moderacoes) {
        this.eventoId = eventoId;
        this.eventoTitulo = eventoTitulo;
        this.totalInscricoes = totalInscricoes;
        this.inscricoesAtivas = inscricoesAtivas;
        this.cancelamentos = cancelamentos;
        this.presentes = presentes;
        this.certificadosLiberados = certificadosLiberados;
        this.moderacoes = moderacoes;
    }
    public Integer getEventoId() { return eventoId; }
    public String getEventoTitulo() { return eventoTitulo; }
    public long getTotalInscricoes() { return totalInscricoes; }
    public long getInscricoesAtivas() { return inscricoesAtivas; }
    public long getCancelamentos() { return cancelamentos; }
    public long getPresentes() { return presentes; }
    public long getCertificadosLiberados() { return certificadosLiberados; }
    public long getModeracoes() { return moderacoes; }
}
