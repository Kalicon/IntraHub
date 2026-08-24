package com.example.demo.model.health;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_atendimentos")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "leito_id")
    private Leito leito;

    @Column(nullable = false)
    private LocalDateTime dataEntrada;

    private LocalDateTime dataAlta;

    @Column(nullable = false)
    private String queixaPrincipal;

    private String corTriagem; // VERMELHO, LARANJA, AMARELO, VERDE, AZUL
    private Integer tempoMaximoMinutosSLA;

    @Column(nullable = false)
    private String status; // EM_TRIAGEM, EM_ATENDIMENTO, INTERNADO, ALTA, TRANSFERIDO

    private String medicoResponsavel;
    private String diagnosticoProvavel;
}