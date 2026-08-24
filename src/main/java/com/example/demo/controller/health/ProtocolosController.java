package com.example.demo.controller.health;

import com.example.demo.service.health.ClinicalAlertService;
import com.example.demo.repository.health.AtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/health/protocolos")
public class ProtocolosController {

    @Autowired private ClinicalAlertService alertService;
    @Autowired private AtendimentoRepository atendimentoRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("protocolos", alertService.listarTodos());
        model.addAttribute("atendimentos", atendimentoRepository.findAll());
        return "health/protocolos";
    }

    @PostMapping("/abrir")
    public String abrirProtocolo(@RequestParam Long atendimentoId, @RequestParam String tipoProtocolo, @RequestParam String medico) {
        alertService.abrirProtocolo(atendimentoId, tipoProtocolo, medico);
        return "redirect:/health/protocolos";
    }
}