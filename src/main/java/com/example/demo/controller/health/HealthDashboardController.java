package com.example.demo.controller.health;

import com.example.demo.repository.health.AtendimentoRepository;
import com.example.demo.repository.health.LeitoRepository;
import com.example.demo.repository.health.ProtocoloClinicoRepository;
import com.example.demo.repository.health.SinalVitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/health")
public class HealthDashboardController {

    @Autowired private LeitoRepository leitoRepository;
    @Autowired private AtendimentoRepository atendimentoRepository;
    @Autowired private ProtocoloClinicoRepository protocoloRepository;
    @Autowired private SinalVitalRepository sinalVitalRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalLeitos = leitoRepository.count();
        long leitosOcupados = leitoRepository.countByStatus("OCUPADO");
        long leitosVagos = leitoRepository.countByStatus("VAGO");
        long leitosHigienizacao = leitoRepository.countByStatus("HIGIENIZACAO");

        double taxaOcupacao = totalLeitos > 0 ? ((double) leitosOcupados / totalLeitos) * 100.0 : 0.0;

        model.addAttribute("totalLeitos", totalLeitos);
        model.addAttribute("leitosOcupados", leitosOcupados);
        model.addAttribute("leitosVagos", leitosVagos);
        model.addAttribute("leitosHigienizacao", leitosHigienizacao);
        model.addAttribute("taxaOcupacao", String.format("%.1f", taxaOcupacao));

        model.addAttribute("atendimentosAtivos", atendimentoRepository.findByStatus("EM_ATENDIMENTO").size() + atendimentoRepository.findByStatus("INTERNADO").size());
        model.addAttribute("protocolosAbertos", protocoloRepository.findByStatus("ABERTO").size());
        model.addAttribute("pacientesCriticosNEWS2", sinalVitalRepository.findByNivelRiscoNEWS2("ALTO").size());

        return "health/dashboard";
    }
}