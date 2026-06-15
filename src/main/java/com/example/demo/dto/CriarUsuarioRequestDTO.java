package com.example.demo.dto;

// DTO para a criação de um novo usuário. Garante que apenas os dados necessários
// sejam expostos na API.
public class CriarUsuarioRequestDTO {
    private Long hospitalId;
    private String login;
    private String senha;
    private String papel;

    // Getters e Setters
    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }
}