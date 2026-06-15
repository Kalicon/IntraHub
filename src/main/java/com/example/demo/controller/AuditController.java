package com.example.demo.controller;

import com.example.demo.model.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/admin/auditoria")
@PreAuthorize("hasRole('ADMIN')") // Garante que só ADMIN acessa
public class AuditController {

    @Autowired private AuditLogRepository repo;

    // 1. Listar para o Modal
    @GetMapping
    public List<AuditLog> listar() {
        return repo.findAllByOrderByDataHoraDesc();
    }

    // 2. Baixar CSV
    @GetMapping("/download")
    public void downloadCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8"); // Força UTF-8 para acentos
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=auditoria_log.csv");

        // Adiciona BOM para o Excel abrir acentos corretamente
        response.getWriter().write('\ufeff');

        PrintWriter writer = response.getWriter();
        writer.println("ID;Data/Hora;Usuário;Ação;Módulo;Detalhes");

        List<AuditLog> logs = repo.findAllByOrderByDataHoraDesc();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        for (AuditLog log : logs) {
            writer.println(
                    log.getId() + ";" +
                            log.getDataHora().format(fmt) + ";" +
                            log.getUsuario() + ";" +
                            log.getAcao() + ";" +
                            log.getModulo() + ";" +
                            log.getDetalhes()
            );
        }
    }
}