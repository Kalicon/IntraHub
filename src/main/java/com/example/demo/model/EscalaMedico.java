package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_escala_medico")
public class EscalaMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "escala_id")
    @JsonIgnore
    private Escala escala;

    @ManyToOne
    @JoinColumn(name = "setor_id")
    private Setor setor;

    private String medico;
    private String ramal;
}