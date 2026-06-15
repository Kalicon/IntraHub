package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;

@Data @Entity @Table(name = "tb_cardapios")
public class Cardapio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String diaSemana;
    private String data;
    private String pratoPrincipal;
    @Column(columnDefinition = "TEXT") private String acompanhamento;
}