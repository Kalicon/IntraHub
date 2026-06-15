package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hospitalId = 1L;

    @Column(unique = true)
    private String login;
    private String senha;
    private String papel; // USER, ADMIN, ESCALA, NUTRICAO, MANUTENCAO

    // Novo campo: Se false, ele não consegue logar até o Admin aprovar
    private boolean ativo = false;

    // Vínculo opcional com funcionário (para saber quem é quem)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    private Funcionario funcionario;

    public Usuario() {}

    public Usuario(String login, String senha, String papel, boolean ativo) {
        this.login = login;
        this.senha = senha;
        this.papel = papel;
        this.ativo = ativo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
}