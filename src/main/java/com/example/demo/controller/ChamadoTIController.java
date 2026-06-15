package com.example.demo.controller;

import com.example.demo.model.ChamadoTI;
import com.example.demo.repository.ChamadoTIRepository;
import com.example.demo.service.AuditService;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoTIController {

    @Autowired private ChamadoTIRepository repository;
    @Autowired private EmailService emailService;
    @Autowired private AuditService auditService;

    @GetMapping
    public List<ChamadoTI> listar() {
        return repository.findAllByOrderByDataAberturaDesc();
    }

    @PostMapping
    public ChamadoTI abrir(@RequestBody ChamadoTI c) {
        c.setDataAbertura(LocalDateTime.now());
        c.setStatus("Aberto");
        ChamadoTI salvo = repository.save(c);
        try { emailService.notificarTI(c.getSolicitante(), c.getRamal(), c.getTitulo(), c.getDescricao()); } catch (Exception e) {}
        return salvo;
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<?> concluir(@PathVariable Long id) {
        return repository.findById(id).map(c -> {
            c.setStatus("Concluído");
            c.setDataConclusao(LocalDateTime.now());
            repository.save(c);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.findById(id).ifPresent(c -> {
            auditService.registrar("EXCLUSÃO", "TI", "Chamado apagado: " + c.getTitulo());
            repository.deleteById(id);
        });
    }
} // Chave final ok