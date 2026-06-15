package com.example.demo.controller;

import com.example.demo.model.Achado;
import com.example.demo.repository.AchadoRepository; // Import correto
import com.example.demo.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/achados")
public class AchadoController {

    @Autowired private AchadoRepository repo;
    @Autowired private AuditService auditService;

    @GetMapping
    public List<Achado> listar() { return repo.findByStatus("Aguardando"); }

    @PostMapping
    public Achado registrar(@RequestBody Achado a) {
        a.setStatus("Aguardando");
        return repo.save(a);
    }

    @PutMapping("/{id}/retirar")
    public void retirar(@PathVariable Long id) {
        repo.findById(id).ifPresent(a -> {
            a.setStatus("Entregue");
            repo.save(a);
            auditService.registrar("ALTERAÇÃO", "ACHADOS", "Item entregue: " + a.getOQue());
        });
    }
}