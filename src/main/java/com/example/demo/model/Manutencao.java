package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_manutencao")
public class Manutencao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String solicitante;
    private String setor;
    private String tipo; // Elétrica, Hidráulica...
    private String descricao;
    private String prioridade;
    private String status;

    private LocalDateTime dataAbertura;
    private LocalDateTime dataConclusao;
}