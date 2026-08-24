package com.example.demo.controller.health;

import com.example.demo.model.health.EventoAdverso;
import com.example.demo.service.health.PatientSafetyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/health/incidentes")
public class IncidentesController {

    @Autowired private PatientSafetyService safetyService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("incidentes", safetyService.listarNotificacoes());
        return "health/incidentes";
    }

    @PostMapping("/salvar")
    public String salvarIncidente(@ModelAttribute EventoAdverso evento) {
        safetyService.registrarNotificacao(evento);
        return "redirect:/health/incidentes";
    }
}