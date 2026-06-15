package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_escala_enfermagem")
public class EscalaEnfermagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // AQUI ESTAVA O PROBLEMA DO HIBERNATE: O nome deve ser 'escala'
    @ManyToOne
    @JoinColumn(name = "escala_id")
    @JsonIgnore
    private Escala escala;

    @ManyToOne
    @JoinColumn(name = "setor_id")
    private Setor setor;

    private String supervisor; // Nome do profissional
    private String ramal;
}