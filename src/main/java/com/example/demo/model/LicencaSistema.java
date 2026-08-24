package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_licenca_sistema")
public class LicencaSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clienteRazaoSocial;
    private String cnpj;
    
    @Column(columnDefinition = "TEXT")
    private String chaveLicenca; // Master key string

    private LocalDate dataEmissao;
    private LocalDate dataValidade;

    private Integer limiteUsuarios;
    private String plano; // "BASIC", "PRO", "ENTERPRISE_HEALTH"

    private boolean ativa = true;
    private LocalDateTime dataUltimaChecagem;
}