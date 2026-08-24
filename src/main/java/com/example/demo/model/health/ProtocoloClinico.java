package com.example.demo.model.health;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_protocolos_clinicos")
public class ProtocoloClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "atendimento_id", nullable = false)
    private Atendimento atendimento;

    @Column(nullable = false)
    private String tipoProtocolo; // SEPSE, AVC, IAM, DETERIORACAO_CLINICA

    private LocalDateTime dataAbertura;
    private LocalDateTime dataConclusao;
    private String status; // ABERTO, EM_ANDAMENTO, CONCLUIDO, CANCELADO

    private Integer tempoGastoMinutos;
    private Integer metaSlaMinutos; // ex: Sepse 60min, AVC 45min, IAM 90min

    @Column(columnDefinition = "TEXT")
    private String condutasAdotadas;

    private String medicoResponsavel;
}