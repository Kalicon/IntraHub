package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "tb_achados")
public class Achado {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String oQue; // Ex: "Chave de carro", "RG"
    private String onde; // Ex: "Recepção Central"
    private String quemAchou; // Nome da pessoa
    private LocalDate data = LocalDate.now();
    private String status = "Aguardando"; // "Aguardando", "Entregue"

    @Column(columnDefinition = "TEXT")
    private String foto; // Base64 (opcional)
}