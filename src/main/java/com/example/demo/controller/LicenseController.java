package com.example.demo.controller;

import com.example.demo.model.LicencaSistema;
import com.example.demo.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class LicenseController {

    @Autowired private LicenseService licenseService;

    @GetMapping("/licenca")
    public String paginaLicenca(Model model) {
        Optional<LicencaSistema> licOpt = licenseService.getLicencaAtiva();
        boolean valida = licenseService.isLicencaValida();
        long diasRestantes = licenseService.getDiasRestantes();

        model.addAttribute("licenca", licOpt.orElse(null));
        model.addAttribute("valida", valida);
        model.addAttribute("diasRestantes", diasRestantes);

        return "licenca";
    }

    @GetMapping("/admin/licenciador")
    public String painelLicenciadorMaster(Model model) {
        Optional<LicencaSistema> licOpt = licenseService.getLicencaAtiva();
        model.addAttribute("licenca", licOpt.orElse(null));
        return "admin/licenciador";
    }

    @GetMapping("/api/v1/licenca/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> statusLicenca() {
        Map<String, Object> resp = new HashMap<>();
        Optional<LicencaSistema> licOpt = licenseService.getLicencaAtiva();
        boolean valida = licenseService.isLicencaValida();
        long diasRestantes = licenseService.getDiasRestantes();

        resp.put("valida", valida);
        resp.put("diasRestantes", diasRestantes);
        if (licOpt.isPresent()) {
            LicencaSistema lic = licOpt.get();
            resp.put("cliente", lic.getClienteRazaoSocial());
            resp.put("cnpj", lic.getCnpj());
            resp.put("validade", lic.getDataValidade().toString());
            resp.put("plano", lic.getPlano());
            resp.put("limiteUsuarios", lic.getLimiteUsuarios());
            resp.put("ultimaChecagem", lic.getDataUltimaChecagem() != null ? lic.getDataUltimaChecagem().toString() : "N/A");
        }

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/api/v1/licenca/ativar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> ativarChave(@RequestBody Map<String, String> body) {
        Map<String, Object> resp = new HashMap<>();
        String chave = body.get("chaveLicenca");

        if (chave == null || chave.trim().isEmpty()) {
            resp.put("sucesso", false);
            resp.put("mensagem", "Chave de licença não informada.");
            return ResponseEntity.badRequest().body(resp);
        }

        boolean ok = licenseService.ativarChave(chave);
        if (ok) {
            resp.put("sucesso", true);
            resp.put("mensagem", "Licença validada e ativada com sucesso no sistema!");
            return ResponseEntity.ok(resp);
        } else {
            resp.put("sucesso", false);
            resp.put("mensagem", "Chave de licença inválida, adulterada ou com formato incorreto.");
            return ResponseEntity.badRequest().body(resp);
        }
    }

    @PostMapping("/api/v1/licenca/gerar-master")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> gerarChaveMaster(@RequestBody Map<String, Object> body) {
        Map<String, Object> resp = new HashMap<>();
        String cnpj = (String) body.getOrDefault("cnpj", "12345678000199");
        String razaoSocial = (String) body.getOrDefault("razaoSocial", "Cliente Corporativo IntraHub");
        String plano = (String) body.getOrDefault("plano", "ANUAL");

        String novaChave = licenseService.gerarChavePlano(cnpj, razaoSocial, plano);
        resp.put("sucesso", true);
        resp.put("chaveGerada", novaChave);
        resp.put("cnpj", cnpj);
        resp.put("razaoSocial", razaoSocial);
        resp.put("plano", plano);

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/api/v1/licenca/sincronizar-online")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sincronizarOnline() {
        Map<String, Object> resp = new HashMap<>();
        boolean ok = licenseService.sincronizarLicencaOnline();
        resp.put("sucesso", ok);
        resp.put("mensagem", ok ? "Licença sincronizada online com sucesso!" : "Falha ao sincronizar com servidor remoto de licença.");
        resp.put("diasRestantes", licenseService.getDiasRestantes());
        return ResponseEntity.ok(resp);
    }
}