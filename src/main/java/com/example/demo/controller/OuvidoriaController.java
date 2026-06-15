package com.example.demo.controller;

import com.example.demo.model.Ouvidoria;
import com.example.demo.repository.OuvidoriaRepository;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@Controller // Note que é @Controller, não @RestController, para servir telas
@RequestMapping("/ouvidoria")
public class OuvidoriaController {

    @Autowired private OuvidoriaRepository repo;
    @Autowired private EmailService emailService; // Aproveitando seu serviço de e-mail

    // 1. Abre a página HTML separada
    @GetMapping
    public ModelAndView abrirPagina() {
        return new ModelAndView("ouvidoria"); // Vai procurar templates/ouvidoria.html
    }

    // 2. API para salvar o relato (JSON)
    @PostMapping("/api/enviar")
    @ResponseBody
    public ResponseEntity<?> enviar(@RequestBody Ouvidoria o) {
        o.setDataEnvio(LocalDateTime.now());
        o.setStatus("Pendente");

        if (o.isAnonimo()) {
            o.setNome("Anônimo");
            o.setSetor("Não informado");
        }

        repo.save(o);

        // Notifica o RH/Admin por e-mail (Segurança)
        try {
            String assunto = "📢 Nova Ouvidoria: " + o.getTipo();
            String corpo = "Novo registro no canal de ética.\n\n" +
                    "Tipo: " + o.getTipo() + "\n" +
                    "Autor: " + (o.isAnonimo() ? "Anônimo" : o.getNome()) + "\n" +
                    "Mensagem:\n" + o.getMensagem();

            // Mande para o e-mail do admin definido no EmailService
            // emailService.enviarEmail("admin@hospital.com", assunto, corpo);
            // (Descomente se quiser ativar)
        } catch (Exception e) {
            System.err.println("Erro e-mail ouvidoria: " + e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    // 3. API para listar (Apenas Admin vê isso na tela)
    @GetMapping("/api/listar")
    @ResponseBody
    public List<Ouvidoria> listar() {
        return repo.findAllByOrderByDataEnvioDesc();
    }

    // 4. API para alterar status
    @PutMapping("/api/status/{id}")
    @ResponseBody
    public void alterarStatus(@PathVariable Long id, @RequestBody String novoStatus) {
        repo.findById(id).ifPresent(o -> {
            o.setStatus(novoStatus.replace("\"", "")); // Remove aspas extras do JSON string
            repo.save(o);
        });
    }
}