package com.example.demo.controller;

import com.example.demo.model.Link;
// A CORREÇÃO ESTÁ NESTE IMPORT:
import com.example.demo.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/links")
public class LinkController {

    @Autowired
    private LinkRepository repository;

    // Listar todos os links
    @GetMapping
    public List<Link> listar() {
        return repository.findAll();
    }

    // Criar um novo link
    @PostMapping
    public Link criar(@RequestBody Map<String, String> payload) {
        Link link = new Link();
        link.setNome(payload.get("nome"));
        link.setUrl(payload.get("url"));
        link.setClasseIcone(payload.get("classeIcone"));
        return repository.save(link);
    }

    // Deletar um link
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}