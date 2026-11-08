package com.example.demoApiVersao23082026.dto;

import java.time.LocalDateTime;

public class UsuarioDTO {

    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private String perfil;
    private Boolean ativo;
    private LocalDateTime dataCadastro;

    public UsuarioDTO() {
    }

    public UsuarioDTO(
            Integer id,
            String nome,
            String email,
            String telefone,
            String perfil,
            Boolean ativo,
            LocalDateTime dataCadastro) {

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.perfil = perfil;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}