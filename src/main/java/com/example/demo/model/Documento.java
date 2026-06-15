package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;

    // Pastas: 'medica', 'enf', 'adm', 'treina'
    private String pasta;

    // Tipo: 'arquivo' ou 'video'
    private String tipo;

    // Se for vídeo
    private String linkVideo;

    // Se for Arquivo (PDF, Doc, etc)
    private String nomeArquivo;
    private String contentType; // ex: application/pdf

    @Lob // Grava o arquivo no banco (BLOB)
    @Column(length = 10000000) // Aumenta limite se necessário
    private byte[] dados;

    private LocalDate dataUpload = LocalDate.now();
}