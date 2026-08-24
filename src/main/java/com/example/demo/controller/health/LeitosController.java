package com.example.demo.controller.health;

import com.example.demo.service.health.BedManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/health/leitos")
public class LeitosController {

    @Autowired private BedManagementService bedService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("leitos", bedService.listarTodosLeitos());
        return "health/leitos";
    }

    @PostMapping("/status")
    public String atualizarStatus(@RequestParam Long leitoId, @RequestParam String status) {
        bedService.atualizarStatusLeito(leitoId, status);
        return "redirect:/health/leitos";
    }
}