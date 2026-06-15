package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_links")
public class Link {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String url;
    private String classeIcone; // Ex: "bi-envelope-at text-warning"

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getClasseIcone() { return classeIcone; }
    public void setClasseIcone(String classeIcone) { this.classeIcone = classeIcone; }
}