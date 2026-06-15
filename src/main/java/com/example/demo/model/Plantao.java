package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_plantoes")
public class Plantao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String nome;
    private String detalhe;
    private String icone;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDetalhe() { return detalhe; }
    public void setDetalhe(String detalhe) { this.detalhe = detalhe; }
    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }
}