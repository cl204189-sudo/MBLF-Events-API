package com.example.demoApiVersao23082026.dto;

public class EventoColaboradorDTO {

    private Integer id;
    private Integer eventoId;
    private String eventoTitulo;

    private Integer usuarioId;
    private String usuarioNome;

    private String funcao;
    private String status;

    private Boolean podeChat;
    private Boolean podeCalendario;
    private Boolean podeTarefas;

    private Boolean podeVerInscritos;
    private Boolean podeRegistrarPresencas;
    private Boolean podeVerRelatorios;

    private Boolean podeSilenciar;
    private Boolean podeExpulsar;
    private Boolean podeLiberarCertificados;

    private Boolean silenciado;

    public EventoColaboradorDTO() {
    }

    public EventoColaboradorDTO(Integer id, Integer eventoId, String eventoTitulo, Integer usuarioId,
                                String usuarioNome, String funcao, String status,
                                Boolean podeChat, Boolean podeCalendario, Boolean podeTarefas,
                                Boolean podeVerInscritos, Boolean podeRegistrarPresencas,
                                Boolean podeVerRelatorios, Boolean podeSilenciar, Boolean podeExpulsar,
                                Boolean podeLiberarCertificados, Boolean silenciado) {
        this.id = id;
        this.eventoId = eventoId;
        this.eventoTitulo = eventoTitulo;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.funcao = funcao;
        this.status = status;
        this.podeChat = podeChat;
        this.podeCalendario = podeCalendario;
        this.podeTarefas = podeTarefas;
        this.podeVerInscritos = podeVerInscritos;
        this.podeRegistrarPresencas = podeRegistrarPresencas;
        this.podeVerRelatorios = podeVerRelatorios;
        this.podeSilenciar = podeSilenciar;
        this.podeExpulsar = podeExpulsar;
        this.podeLiberarCertificados = podeLiberarCertificados;
        this.silenciado = silenciado;
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

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public String getFuncao() {
        return funcao;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEventoId(Integer eventoId) {
        this.eventoId = eventoId;
    }

    public void setEventoTitulo(String eventoTitulo) {
        this.eventoTitulo = eventoTitulo;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public void setStatus(String status) {
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
}