package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_pedidos_material")
public class PedidoMaterial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String solicitante;
    private String setor;
    private LocalDateTime dataPedido = LocalDateTime.now();
    private String status = "Pendente"; // Pendente, Entregue

    @Column(columnDefinition = "TEXT")
    private String listaItens; // JSON simples: "2x Caneta, 1x Papel"
}