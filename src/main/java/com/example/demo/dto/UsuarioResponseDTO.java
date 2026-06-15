package com.example.demo.dto;

import com.example.demo.model.Usuario;

/**
 * DTO para a resposta da API. Garante que dados sensíveis como a senha
 * nunca sejam expostos para o cliente.
 */
public class UsuarioResponseDTO {
    private Long id;
    private Long hospitalId;
    private String login;
    private String papel;
    private boolean ativo;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.hospitalId = usuario.getHospitalId();
        this.login = usuario.getLogin();
        this.papel = usuario.getPapel();
        this.ativo = usuario.isAtivo();
    }

    // Getters
    public Long getId() { return id; }
    public Long getHospitalId() { return hospitalId; }
    public String getLogin() { return login; }
    public String getPapel() { return papel; }
    public boolean isAtivo() { return ativo; }
}