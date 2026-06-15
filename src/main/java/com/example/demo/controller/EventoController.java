package com.example.demo.controller;

import com.example.demo.model.Evento;
import com.example.demo.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoRepository repository;

    // Listar Eventos (Público)
    @GetMapping
    public List<Evento> listar() {
        return repository.findAllByOrderByDataEventoDesc();
    }

    // Criar Evento (Admin)
    @PostMapping
    public ResponseEntity<Evento> criar(@RequestBody Evento evento) {
        try {
            Evento salvo = repository.save(evento);
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Deletar Evento (Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id); // O banco apaga as inscrições automaticamente (Cascade)
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}