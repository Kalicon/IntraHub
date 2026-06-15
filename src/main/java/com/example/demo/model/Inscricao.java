package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_inscricoes")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo usado para salvar o ID vindo do formulário (JSON)
    @Column(name = "evento_id")
    private Long eventoId;

    // CORREÇÃO DO ERRO DE MAPPING:
    // Este campo satisfaz o "mappedBy='evento'" da classe Evento.
    // insertable=false, updatable=false impede conflito com o eventoId acima.
    @ManyToOne
    @JoinColumn(name = "evento_id", insertable = false, updatable = false)
    @JsonIgnore
    private Evento evento;

    private String nome;
    private String cargo;
    private String setor;
    private String email;
    private String instituicao; // Campo Novo

    private LocalDateTime dataInscricao = LocalDateTime.now();

    // --- GETTERS E SETTERS MANUAIS (Para garantir que funcione sem Lombok) ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getInstituicao() { return instituicao; }
    public void setInstituicao(String instituicao) { this.instituicao = instituicao; }

    public LocalDateTime getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(LocalDateTime dataInscricao) { this.dataInscricao = dataInscricao; }
}