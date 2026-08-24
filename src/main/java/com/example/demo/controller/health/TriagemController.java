package com.example.demo.controller.health;

import com.example.demo.model.health.SinalVital;
import com.example.demo.service.health.TriageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/health/triagem")
public class TriagemController {

    @Autowired private TriageService triageService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("atendimentos", triageService.listarAtendimentosAtivos());
        return "health/triagem";
    }

    @PostMapping("/salvar")
    public String salvarTriagem(@RequestParam String cpf,
                                @RequestParam String nome,
                                @RequestParam String queixaPrincipal,
                                @RequestParam String corTriagem,
                                @RequestParam(required = false) Integer fc,
                                @RequestParam(required = false) Integer paSistolica,
                                @RequestParam(required = false) Integer paDiastolica,
                                @RequestParam(required = false) Double temp,
                                @RequestParam(required = false) Integer spo2,
                                @RequestParam(required = false) Integer fr) {

        SinalVital sinais = new SinalVital();
        sinais.setFrequenciaCardiaca(fc);
        sinais.setPressaoSistolica(paSistolica);
        sinais.setPressaoDiastolica(paDiastolica);
        sinais.setTemperatura(temp);
        sinais.setSaturacaoO2(spo2);
        sinais.setFrequenciaRespiratoria(fr);

        triageService.realizarTriagem(cpf, nome, queixaPrincipal, corTriagem, sinais);
        return "redirect:/health/triagem";
    }
}