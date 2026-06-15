package com.example.demo.controller;

import com.example.demo.model.Setor;
import com.example.demo.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/setores")
public class SetorController {

    @Autowired
    private SetorRepository repository;

    @GetMapping
    public List<Setor> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Setor criar(@RequestBody Map<String, String> payload) {
        String nome = payload.get("nome");
        if (nome == null || nome.trim().isEmpty()) {
            throw new RuntimeException("Nome obrigatório");
        }
        Setor s = new Setor();
        s.setNome(nome);
        return repository.save(s);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}