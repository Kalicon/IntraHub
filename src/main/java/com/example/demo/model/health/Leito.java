package com.example.demo.model.health;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_leitos")
public class Leito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo; // ex: UTI-01, ENF-201A

    @Column(nullable = false)
    private String setor; // UTI, Emergencia, Enfermaria, Isolamento

    @Column(nullable = false)
    private String tipo; // UTI, SEMI_INTENSIVA, ENFERMARIA, ISOLAMENTO

    @Column(nullable = false)
    private String status; // VAGO, OCUPADO, HIGIENIZACAO, MANUTENCAO

    private String descricao;
}