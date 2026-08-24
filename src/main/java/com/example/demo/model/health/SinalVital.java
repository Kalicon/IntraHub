package com.example.demo.model.health;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_sinais_vitais")
public class SinalVital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "atendimento_id", nullable = false)
    private Atendimento atendimento;

    private LocalDateTime dataRegistro;
    private Integer frequenciaCardiaca; // bpm
    private Integer pressaoSistolica;   // mmHg
    private Integer pressaoDiastolica;  // mmHg
    private Double temperatura;        // °C
    private Integer saturacaoO2;       // %
    private Integer frequenciaRespiratoria; // rpm
    private Integer glasgow;           // 3-15
    private Boolean usoOxigenioSuplementar;

    private Integer scoreNEWS2;
    private String nivelRiscoNEWS2; // BAIXO, MEDIO, ALTO

    @PrePersist
    @PreUpdate
    public void calcularScoreNEWS2() {
        int score = 0;

        // Frequência Respiratória
        if (frequenciaRespiratoria != null) {
            if (frequenciaRespiratoria <= 8 || frequenciaRespiratoria >= 25) score += 3;
            else if (frequenciaRespiratoria >= 21) score += 2;
            else if (frequenciaRespiratoria >= 9 && frequenciaRespiratoria <= 11) score += 1;
        }

        // Saturacao O2
        if (saturacaoO2 != null) {
            if (saturacaoO2 <= 91) score += 3;
            else if (saturacaoO2 <= 93) score += 2;
            else if (saturacaoO2 <= 95) score += 1;
        }

        // Oxigênio Suplementar
        if (Boolean.TRUE.equals(usoOxigenioSuplementar)) score += 2;

        // Pressao Sistolica
        if (pressaoSistolica != null) {
            if (pressaoSistolica <= 90 || pressaoSistolica >= 220) score += 3;
            else if (pressaoSistolica <= 100) score += 2;
            else if (pressaoSistolica <= 110) score += 1;
        }

        // Frequencia Cardiaca
        if (frequenciaCardiaca != null) {
            if (frequenciaCardiaca <= 40 || frequenciaCardiaca >= 131) score += 3;
            else if (frequenciaCardiaca >= 111) score += 2;
            else if (frequenciaCardiaca <= 50 || (frequenciaCardiaca >= 91 && frequenciaCardiaca <= 110)) score += 1;
        }

        // Temperatura
        if (temperatura != null) {
            if (temperatura <= 35.0) score += 3;
            else if (temperatura >= 39.1) score += 2;
            else if (temperatura <= 36.0 || (temperatura >= 38.1 && temperatura <= 39.0)) score += 1;
        }

        this.scoreNEWS2 = score;
        if (score >= 7) this.nivelRiscoNEWS2 = "ALTO";
        else if (score >= 5) this.nivelRiscoNEWS2 = "MEDIO";
        else this.nivelRiscoNEWS2 = "BAIXO";
    }
}