package com.example.demo.controller;

import com.example.demo.model.Inscricao;
import com.example.demo.repository.InscricaoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter; // Importante para formatar a data
import java.util.List;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    @Autowired private InscricaoRepository repo;

    @PostMapping
    public Inscricao inscrever(@RequestBody Inscricao i) {
        return repo.save(i);
    }

    @GetMapping("/evento/{id}/lista")
    public void baixarLista(@PathVariable Long id, HttpServletResponse response) throws IOException {
        // 1. Define que é um arquivo CSV UTF-8
        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=lista_inscritos.csv");

        // 2. BOM para o Excel (Windows) reconhecer acentos automaticamente
        response.getWriter().write('\ufeff');

        PrintWriter writer = response.getWriter();

        // 3. Cabeçalho formatado
        writer.println("Nome;Instituição;Cargo;Setor;E-mail;Data da Inscrição");

        List<Inscricao> lista = repo.findByEventoId(id);

        // 4. Formatador de Data Brasileiro
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Inscricao i : lista) {
            String dataFormatada = (i.getDataInscricao() != null) ? i.getDataInscricao().format(fmt) : "-";

            writer.println(
                    safe(i.getNome()) + ";" +
                            safe(i.getInstituicao()) + ";" +
                            safe(i.getCargo()) + ";" +
                            safe(i.getSetor()) + ";" +
                            safe(i.getEmail()) + ";" +
                            dataFormatada
            );
        }
    }

    // Função auxiliar para evitar 'null' na planilha
    private String safe(String s) { return s == null ? "" : s.replace(";", ","); }
}