package com.example.demo.model.health;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_plantoes_sbar")
public class PlantaoSBAR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "atendimento_id", nullable = false)
    private Atendimento atendimento;

    private LocalDateTime dataHoraTransfere;
    private String profissionalPassando;
    private String profissionalRecebendo;
    private String turno; // MANHA, TARDE, NOITE

    // SBAR Protocol
    @Column(columnDefinition = "TEXT")
    private String situacao;       // S - Situation (Qual o problema imediato?)

    @Column(columnDefinition = "TEXT")
    private String historico;      // B - Background (Contexto clínico do paciente)

    @Column(columnDefinition = "TEXT")
    private String avaliacao;      // A - Assessment (Achados clínicos atuais)

    @Column(columnDefinition = "TEXT")
    private String recomendacao;   // R - Recommendation (Plano de cuidados)

    private String nivelCriticidade; // ESTAVEL, ATENCAO, CRITICO
    private Boolean pendenciasExames;
    private Boolean ordemNaoReanimar;
}