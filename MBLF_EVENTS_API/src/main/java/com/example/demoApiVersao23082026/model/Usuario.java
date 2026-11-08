package com.example.demoApiVersao23082026.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 512)
    private String nome;

    @Column(name = "nome_criptografado", nullable = false)
    private Boolean nomeCriptografado = false;

    @Column(nullable = false, unique = true, length = 512)
    private String email;

    @Column(name = "email_hash", length = 64, unique = true)
    private String emailHash;

    @Column(name = "email_criptografado", nullable = false)
    private Boolean emailCriptografado = false;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(length = 255)
    private String telefone;

    @Column(name = "telefone_criptografado", nullable = false)
    private Boolean telefoneCriptografado = false;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    @Column
    private Boolean ativo = true;

    @Column(name = "data_cadastro", insertable = false, updatable = false)
    private LocalDateTime dataCadastro;

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

    public Boolean getNomeCriptografado() {
        return nomeCriptografado;
    }

    public void setNomeCriptografado(Boolean nomeCriptografado) {
        this.nomeCriptografado = nomeCriptografado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    public Boolean getEmailCriptografado() {
        return emailCriptografado;
    }

    public void setEmailCriptografado(Boolean emailCriptografado) {
        this.emailCriptografado = emailCriptografado;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getTelefoneCriptografado() {
        return telefoneCriptografado;
    }

    public void setTelefoneCriptografado(Boolean telefoneCriptografado) {
        this.telefoneCriptografado = telefoneCriptografado;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
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
