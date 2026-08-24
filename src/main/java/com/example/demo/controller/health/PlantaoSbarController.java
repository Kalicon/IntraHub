package com.example.demo.controller.health;

import com.example.demo.model.health.PlantaoSBAR;
import com.example.demo.service.health.SbarShiftService;
import com.example.demo.repository.health.AtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/health/sbar")
public class PlantaoSbarController {

    @Autowired private SbarShiftService sbarService;
    @Autowired private AtendimentoRepository atendimentoRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("sbarList", sbarService.listarTodos());
        model.addAttribute("atendimentos", atendimentoRepository.findAll());
        return "health/sbar";
    }

    @PostMapping("/salvar")
    public String salvarSbar(@ModelAttribute PlantaoSBAR sbar, @RequestParam Long atendimentoId) {
        sbar.setAtendimento(atendimentoRepository.findById(atendimentoId).orElseThrow());
        sbarService.registrarSBAR(sbar);
        return "redirect:/health/sbar";
    }
}