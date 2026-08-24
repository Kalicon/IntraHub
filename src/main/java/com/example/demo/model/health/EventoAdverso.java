package com.example.demo.model.health;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_eventos_adversos")
public class EventoAdverso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataOcorrencia;
    private String setor;
    private String tipoIncidente; // QUEDA, ERRO_MEDICACAO, FLEBITE, LESAO_PRESSAO, NEAR_MISS, OUTRO
    private String gravidade; // LEVE, MODERADA, GRAVE, NEAR_MISS

    @Column(columnDefinition = "TEXT")
    private String descricaoDetalhada;

    @Column(columnDefinition = "TEXT")
    private String acaoImediataTomada;

    private Boolean anonimo;
    private String notificador;
    private String statusAnalise; // PENDENTE, EM_ANALISE, PLANO_ACAO_CONCLUIDO
}