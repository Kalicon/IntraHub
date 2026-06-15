package com.example.demo.controller;

import com.example.demo.model.Enquete;
import com.example.demo.repository.EnqueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enquetes")
public class EnqueteController {

    @Autowired
    private EnqueteRepository enqueteRepo;

    // Buscar a enquete ativa
    @GetMapping("/ativa")
    public ResponseEntity<Enquete> getAtiva() {
        return enqueteRepo.findFirstByAtivaTrueOrderByIdDesc()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    // Criar nova (Desativa as anteriores para garantir apenas 1 ativa)
    @PostMapping
    public ResponseEntity<Enquete> criar(@RequestBody Enquete enquete) {
        // Desativa todas as outras antes de criar a nova
        List<Enquete> antigas = enqueteRepo.findAll();
        antigas.forEach(e -> { e.setAtiva(false); enqueteRepo.save(e); });

        enquete.setAtiva(true);
        enquete.setVotos1(0);
        enquete.setVotos2(0);
        return ResponseEntity.ok(enqueteRepo.save(enquete));
    }

    // Votar
    @PostMapping("/{id}/votar/{opcao}")
    public ResponseEntity<Enquete> votar(@PathVariable Long id, @PathVariable int opcao) {
        return enqueteRepo.findById(id).map(e -> {
            if (opcao == 1) e.setVotos1(e.getVotos1() + 1);
            else if (opcao == 2) e.setVotos2(e.getVotos2() + 1);
            return ResponseEntity.ok(enqueteRepo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Deletar (Para limpar a tela)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if(enqueteRepo.existsById(id)) {
            enqueteRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}