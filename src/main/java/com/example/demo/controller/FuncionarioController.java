package com.example.demo.controller;

import com.example.demo.model.Funcionario;
import com.example.demo.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired private FuncionarioRepository repository;

    @GetMapping
    public List<Funcionario> listar() {
        return repository.findByAtivoTrueOrderByNomeAsc();
    }

    @PostMapping
    public Funcionario salvar(@RequestBody Funcionario f) {
        System.out.println(">>> Salvando funcionário: " + f.getNome());
        return repository.save(f);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }

    // Endpoint para relatório Excel simples (Opcional, redireciona pra lista)
    @GetMapping("/relatorio")
    public void baixarRelatorio() {
        // Implementação futura ou redirecionar
    }
}