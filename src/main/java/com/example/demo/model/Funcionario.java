package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "tb_funcionarios")
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String ramal;
    private String cargo;
    private String setor;
    private LocalDate dataNascimento; // Formato YYYY-MM-DD

    @Column(columnDefinition = "TEXT") // Permite fotos grandes no Postgres
    private String foto; // Base64

    private boolean ativo = true;
}