package com.example.demo.controller;

import com.example.demo.model.Album;
import com.example.demo.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/albuns")
public class AlbumController {

    @Autowired
    private AlbumRepository repository;

    @GetMapping
    public List<Album> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Album criar(@RequestBody Map<String, String> payload) {
        Album a = new Album();
        a.setTitulo(payload.get("titulo"));
        a.setCapa(payload.get("capa")); // Base64 ou URL da imagem
        a.setDataCriacao(LocalDate.now());
        return repository.save(a);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}