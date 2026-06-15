package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_reservas")
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String solicitante;
    private String ramal;
    private Long salaId;      // ID da sala vinculada
    private String salaNome;  // Nome da sala (para facilitar exibição)
    private String dataHoraTexto; // Texto livre: "Dia 25/01 as 14h"
    private String motivo;
}