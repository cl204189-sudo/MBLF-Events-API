package com.example.demoApiVersao23082026.dto;

public class LoginResponseDTO {

    private Integer id;
    private String nome;
    private String email;
    private String perfil;

    private String token;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(
            Integer id,
            String nome,
            String email,
            String perfil,
            String token) {

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPerfil() {
        return perfil;
    }

    public String getToken() {
        return token;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public void setToken(String token) {
        this.token = token;
    }
}