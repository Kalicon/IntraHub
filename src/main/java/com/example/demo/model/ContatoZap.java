package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_contatos_zap")
public class ContatoZap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo; // Ex: "RH - Desenvolvimento"
    private String link;   // Ex: https://chat.whatsapp.com/... ou https://wa.me/5511...

    @Column(columnDefinition = "TEXT")
    private String qrCode; // Imagem em Base64
}