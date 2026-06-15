package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hospitalId;

    @Column
    private Long usuarioId; // Nulo para ações do sistema

    @Column
    private String usuario;

    @Column(nullable = false)
    private String acao;

    @Column
    private String entidade;

    @Column
    private Long entidadeId;

    @Lob // Large Object - ideal para campos TEXT/CLOB
    @Column(columnDefinition = "TEXT")
    private String valorAntigo; // JSON do objeto antes

    @Lob
    @Column(columnDefinition = "TEXT")
    private String valorNovo; // JSON do objeto depois

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

    // Construtor para facilitar a criação do log
    public AuditLog(Long hospitalId, Long usuarioId, String acao, String entidade, Long entidadeId, String valorAntigo, String valorNovo) {
        this.hospitalId = hospitalId;
        this.usuarioId = usuarioId;
        this.acao = acao;
        this.entidade = entidade;
        this.entidadeId = entidadeId;
        this.valorAntigo = valorAntigo;
        this.valorNovo = valorNovo;
    }

    public AuditLog() {
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }
    public String getEntidade() { return entidade; }
    public void setEntidade(String entidade) { this.entidade = entidade; }
    public String getModulo() { return entidade; }
    public Long getEntidadeId() { return entidadeId; }
    public void setEntidadeId(Long entidadeId) { this.entidadeId = entidadeId; }
    public String getValorAntigo() { return valorAntigo; }
    public void setValorAntigo(String valorAntigo) { this.valorAntigo = valorAntigo; }
    public String getValorNovo() { return valorNovo; }
    public void setValorNovo(String valorNovo) { this.valorNovo = valorNovo; }
    public String getDetalhes() { return valorNovo; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}