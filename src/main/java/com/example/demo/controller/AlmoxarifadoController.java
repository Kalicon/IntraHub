package com.example.demo.controller;

import com.example.demo.model.Material;
import com.example.demo.model.PedidoMaterial;
// ATENÇÃO: Os imports abaixo devem começar com com.example.demo...
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.PedidoMaterialRepository;
import com.example.demo.service.AuditService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping("/almoxarifado")
public class AlmoxarifadoController {

    @Autowired private MaterialRepository matRepo;
    @Autowired private PedidoMaterialRepository pedRepo;
    @Autowired private AuditService auditService;

    private static boolean SISTEMA_ATIVO = true;

    @GetMapping
    public ModelAndView abrirPagina() {
        if (!SISTEMA_ATIVO) return new ModelAndView("redirect:/?aviso=almoxarifado_off");
        return new ModelAndView("materiais");
    }

    @PostMapping("/api/toggle")
    @ResponseBody
    public boolean toggleSistema() {
        SISTEMA_ATIVO = !SISTEMA_ATIVO;
        auditService.registrar("ALTERAÇÃO", "ALMOXARIFADO", "Sistema " + (SISTEMA_ATIVO ? "Ativado" : "Desativado"));
        return SISTEMA_ATIVO;
    }

    @GetMapping("/api/status")
    @ResponseBody
    public boolean getStatus() { return SISTEMA_ATIVO; }

    @GetMapping("/api/materiais")
    @ResponseBody
    public List<Material> listarMateriais() { return matRepo.findAll(); }

    @PostMapping("/api/materiais")
    @ResponseBody
    public Material salvarMaterial(@RequestBody Material m) { return matRepo.save(m); }

    @GetMapping("/api/pedidos")
    @ResponseBody
    public List<PedidoMaterial> listarPedidos() { return pedRepo.findAllByOrderByDataPedidoDesc(); }

    @PostMapping("/api/pedidos")
    @ResponseBody
    public PedidoMaterial criarPedido(@RequestBody PedidoMaterial p) { return pedRepo.save(p); }

    @PutMapping("/api/pedidos/{id}/concluir")
    @ResponseBody
    public void concluirPedido(@PathVariable Long id) {
        pedRepo.findById(id).ifPresent(p -> {
            p.setStatus("Entregue");
            pedRepo.save(p);
        });
    }
}