package com.example.demo.controller;

import com.example.demo.model.Escala;
import com.example.demo.service.EscalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/escala")
public class EscalaController {

    private final EscalaService escalaService;

    @Autowired
    public EscalaController(EscalaService escalaService) {
        this.escalaService = escalaService;
    }

    // --- BUSCAR ESCALA DO DIA ---
    @GetMapping("/{data}")
    public Escala obterPorData(@PathVariable String data) {
        return escalaService.obterPorData(data);
    }

    // --- SALVAR ESCALA ---
    @PutMapping("/{id}")
    public Escala salvar(@PathVariable Long id, @RequestBody Escala dadosTela) {
        return escalaService.salvar(id, dadosTela);
    }

    // --- REPLICAR ---
    @PostMapping("/replicar/{id}")
    public void replicarEscala(@PathVariable Long id) {
        escalaService.replicarEscala(id);
    }
}