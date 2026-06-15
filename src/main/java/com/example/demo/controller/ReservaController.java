package com.example.demo.controller;

import com.example.demo.model.Reserva;
import com.example.demo.model.Sala;
import com.example.demo.repository.ReservaRepository;
import com.example.demo.repository.SalaRepository;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired private ReservaRepository repo;
    @Autowired private SalaRepository salaRepo;
    @Autowired private EmailService emailService;

    @GetMapping
    public List<Reserva> listar() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Reserva r) {
        // Busca o nome da sala para o e-mail ficar legível
        String nomeSala = salaRepo.findById(r.getSalaId())
                .map(Sala::getNome)
                .orElse("Sala não identificada");

        r.setSalaNome(nomeSala);

        Reserva salva = repo.save(r);

        // Disparo de E-mail
        try {
            emailService.notificarSolicitacaoReserva(r.getSolicitante(), r.getRamal(), nomeSala, r.getDataHoraTexto(), r.getMotivo());
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail Reserva: " + e.getMessage());
        }

        return ResponseEntity.ok(salva);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}