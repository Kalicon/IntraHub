package com.example.demo.controller;

import com.example.demo.model.ContatoZap;
import com.example.demo.repository.ContatoZapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/whatsapp")
public class ContatoZapController {

    @Autowired
    private ContatoZapRepository repository;

    @GetMapping
    public List<ContatoZap> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ContatoZap salvar(@RequestBody ContatoZap c) {
        return repository.save(c);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}