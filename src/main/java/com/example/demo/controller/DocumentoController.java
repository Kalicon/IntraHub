package com.example.demo.controller;

import com.example.demo.model.Documento;
import com.example.demo.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documentos")
@Transactional
public class DocumentoController {

    @Autowired private DocumentoRepository repository;

    @GetMapping("/pasta/{nomePasta}")
    public List<Documento> listarPorPasta(@PathVariable String nomePasta) {
        return repository.findByPastaOrderByDataUploadDesc(nomePasta);
    }

    @PostMapping
    public ResponseEntity<?> upload(
            @RequestParam("titulo") String titulo,
            @RequestParam("descricao") String descricao,
            @RequestParam("pasta") String pasta,
            @RequestParam(value = "arquivo", required = false) MultipartFile arquivo,
            @RequestParam(value = "linkVideo", required = false) String linkVideo
    ) throws IOException {

        Documento doc = new Documento();
        doc.setTitulo(titulo);
        doc.setDescricao(descricao);
        doc.setPasta(pasta);

        if (linkVideo != null && !linkVideo.isEmpty()) {
            doc.setTipo("video");
            doc.setLinkVideo(linkVideo);
        } else if (arquivo != null && !arquivo.isEmpty()) {
            doc.setTipo("arquivo");
            doc.setNomeArquivo(arquivo.getOriginalFilename());
            doc.setContentType(arquivo.getContentType());
            doc.setDados(arquivo.getBytes());
        } else {
            return ResponseEntity.badRequest().body("Envie um arquivo ou link.");
        }

        return ResponseEntity.ok(repository.save(doc));
    }

    // --- NOVO: VISUALIZAR (Abre na aba se for PDF/Imagem) ---
    @GetMapping("/{id}/visualizar")
    public ResponseEntity<byte[]> visualizar(@PathVariable Long id) {
        Documento doc = repository.findById(id).orElse(null);
        if (doc == null || doc.getDados() == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                // "inline" diz ao navegador: Tente renderizar!
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getNomeArquivo() + "\"")
                .contentType(MediaType.parseMediaType(doc.getContentType()))
                .body(doc.getDados());
    }

    // --- BAIXAR (Força o download) ---
    @GetMapping("/{id}/baixar")
    public ResponseEntity<byte[]> baixar(@PathVariable Long id) {
        Documento doc = repository.findById(id).orElse(null);
        if (doc == null || doc.getDados() == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                // "attachment" diz ao navegador: Baixe agora!
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getNomeArquivo() + "\"")
                .contentType(MediaType.parseMediaType(doc.getContentType()))
                .body(doc.getDados());
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}