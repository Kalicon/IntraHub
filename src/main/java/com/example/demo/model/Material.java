package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_materiais")
public class Material {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome; // "Papel A4", "Caneta Azul"
    private String unidade; // "Resma", "Caixa", "Unidade"
    private boolean disponivel = true;
}