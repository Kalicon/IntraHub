package com.example.demo.controller;

import com.example.demo.model.ReservaFrota;
import com.example.demo.repository.ReservaFrotaRepository; // Import correto
import com.example.demo.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/frota")
public class FrotaController {

    @Autowired private ReservaFrotaRepository repo;
    @Autowired private AuditService auditService;

    @GetMapping
    public List<ReservaFrota> listar() { return repo.findAll(); }

    @PostMapping
    public ReservaFrota reservar(@RequestBody ReservaFrota r) {
        return repo.save(r);
    }

    @DeleteMapping("/{id}")
    public void cancelar(@PathVariable Long id) {
        repo.findById(id).ifPresent(r -> {
            auditService.registrar("EXCLUSÃO", "FROTA", "Reserva cancelada: " + r.getVeiculo());
            repo.deleteById(id);
        });
    }
}