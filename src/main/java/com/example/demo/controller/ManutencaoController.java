package com.example.demo.controller;

import com.example.demo.model.Manutencao;
import com.example.demo.repository.ManutencaoRepository;
import com.example.demo.service.AuditService;
import com.example.demo.service.EmailService; // <--- Import do Email
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/manutencao")
public class ManutencaoController {

    @Autowired private ManutencaoRepository repository;
    @Autowired private AuditService auditService;
    @Autowired private EmailService emailService; // <--- Injeção do Email

    @GetMapping
    public List<Manutencao> listar() {
        return repository.findAllByOrderByDataAberturaDesc();
    }

    @PostMapping
    public Manutencao abrir(@RequestBody Manutencao m) {
        m.setDataAbertura(LocalDateTime.now());
        m.setStatus("Aberto");

        Manutencao salvo = repository.save(m);

        // Tenta enviar o e-mail (não trava se falhar)
        try {
            // OBS: Certifique-se que existe o método 'notificarManutencao' no seu EmailService
            // Se não existir, você pode usar um método genérico ou criar lá.
            emailService.notificarManutencao(m.getSolicitante(), m.getSetor(), m.getTipo(), m.getDescricao());
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail de manutenção: " + e.getMessage());
        }

        return salvo;
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<?> concluir(@PathVariable Long id) {
        return repository.findById(id).map(m -> {
            m.setStatus("Concluído");
            m.setDataConclusao(LocalDateTime.now());
            repository.save(m);

            // Log de Conclusão
            auditService.registrar("ALTERAÇÃO", "MANUTENÇÃO", "Concluído: " + m.getTipo() + " em " + m.getSetor());

            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.findById(id).ifPresent(m -> {
            // Log de Exclusão
            auditService.registrar("EXCLUSÃO", "MANUTENÇÃO", "Apagado: " + m.getTipo() + " - " + m.getDescricao());

            repository.deleteById(id);
        });
    }
}