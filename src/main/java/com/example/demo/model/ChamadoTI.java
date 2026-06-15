package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_chamados_ti")
public class ChamadoTI {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String solicitante;
    private String ramal;
    private String local;
    private String titulo;
    private String descricao;
    private String prioridade;
    private String status; // "Aberto", "Concluido"

    private LocalDateTime dataAbertura;
    private LocalDateTime dataConclusao;
}