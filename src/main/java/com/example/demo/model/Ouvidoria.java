package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_ouvidoria")
public class Ouvidoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; // "Denuncia", "Elogio", "Sugestao"

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    private boolean anonimo;
    private String nome; // Opcional (se não for anônimo)
    private String setor; // Opcional

    private String status = "Pendente"; // "Pendente", "Em Análise", "Resolvido"

    private LocalDateTime dataEnvio = LocalDateTime.now();
}