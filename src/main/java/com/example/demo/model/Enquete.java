package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_enquetes")
public class Enquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pergunta;

    private String opcao1;
    private String opcao2;

    private Integer votos1 = 0; // Inicia com 0
    private Integer votos2 = 0; // Inicia com 0

    private boolean ativa = true; // Define se aparece na tela
}