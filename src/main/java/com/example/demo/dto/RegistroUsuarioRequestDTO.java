package com.example.demo.dto;

import lombok.Data;

@Data
public class RegistroUsuarioRequestDTO {
    private String login;
    private String senha;
    private String nome;
    private String email;
    private String ramal;
    private String setor;
    private String cargo;
    private String dataNascimento; // String no formato yyyy-MM-dd que será convertida para LocalDate
    private String foto; // Base64
}
