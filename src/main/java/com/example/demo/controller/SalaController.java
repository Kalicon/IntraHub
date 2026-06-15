package com.example.demo.controller;

import com.example.demo.model.Sala;
import com.example.demo.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/salas")
public class SalaController {

    @Autowired
    private SalaRepository repository;

    @GetMapping
    public List<Sala> listarTodas() {
        return repository.findAll();
    }

    // Lista apenas ativas para o combo de seleção
    @GetMapping("/ativas")
    public List<Sala> listarAtivas() {
        return repository.findByAtivaTrue();
    }

    @PostMapping
    public ResponseEntity<Sala> criar(@RequestBody Map<String, Object> payload) {
        String nome = (String) payload.get("nome");
        if (nome == null || nome.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Sala sala = new Sala();
        sala.setNome(nome);
        // Se vier "ativa", usa, senão padrão true
        sala.setAtiva(payload.containsKey("ativa") ? (Boolean) payload.get("ativa") : true);

        return ResponseEntity.ok(repository.save(sala));
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<?> alternarStatus(@PathVariable Long id) {
        return repository.findById(id).map(sala -> {
            sala.setAtiva(!sala.isAtiva());
            repository.save(sala);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}