package com.example.demo.controller;

import com.example.demo.model.Cardapio;
import com.example.demo.repository.CardapioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cardapios")
public class CardapioController {

    @Autowired private CardapioRepository repository;

    @GetMapping
    public List<Cardapio> listar() {
        return repository.findAll();
    }

    // A CORREÇÃO: O método PUT que estava faltando ou incorreto
    @PutMapping("/{id}")
    public Cardapio atualizar(@PathVariable Long id, @RequestBody Cardapio c) {
        return repository.findById(id).map(existente -> {
            // Atualiza os campos
            existente.setPratoPrincipal(c.getPratoPrincipal());
            existente.setAcompanhamento(c.getAcompanhamento());
            if(c.getData() != null && !c.getData().isEmpty()) {
                existente.setData(c.getData());
            }
            return repository.save(existente);
        }).orElse(null);
    }
}