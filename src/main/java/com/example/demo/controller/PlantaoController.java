package com.example.demo.controller;

import com.example.demo.model.Plantao;
import com.example.demo.repository.PlantaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plantoes")
public class PlantaoController {

    @Autowired
    private PlantaoRepository repository;

    @GetMapping
    public List<Plantao> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Plantao criar(@RequestBody Map<String, String> payload) {
        Plantao p = new Plantao();
        p.setTitulo(payload.get("titulo")); // Ex: "Plantão Noturno UTI"
        p.setNome(payload.get("nome")); // Nome do Link/Arquivo
        p.setDetalhe(payload.get("detalhe")); // URL ou Caminho
        p.setIcone(payload.get("icone"));
        return repository.save(p);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}