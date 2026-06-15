package com.example.demo.controller;

import com.example.demo.model.Aviso;
import com.example.demo.repository.AvisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/avisos")
public class AvisoController {

    @Autowired
    private AvisoRepository repository;

    @GetMapping
    public List<Aviso> listar() {
        // Ordena por ID decrescente (mais novos primeiro)
        return repository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id"));
    }

    @PostMapping
    public Aviso salvar(@RequestBody Aviso aviso) {
        // Gera data atual: "14/01 15:30"
        String agora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
        aviso.setDataPostagem(agora);
        return repository.save(aviso);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}