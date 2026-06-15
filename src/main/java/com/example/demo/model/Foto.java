package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_fotos")
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo;
    private String tipoArquivo;

    // REMOVIDO @Lob para evitar erro de transação no Postgres
    // USANDO TEXT para compatibilidade direta
    @Column(columnDefinition = "TEXT")
    private String dadosBase64;

    @ManyToOne
    @JoinColumn(name = "album_id")
    @JsonIgnore
    private Album album;
}