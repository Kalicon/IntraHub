package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_reserva_frota")
public class ReservaFrota {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String solicitante;
    private String destino;
    private LocalDateTime dataHoraSaida;
    private String veiculo; // Ex: "Ambulância 01", "Carro Adm"
    private String motivo;
}