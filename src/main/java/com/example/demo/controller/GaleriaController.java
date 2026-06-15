package com.example.demo.controller;

import com.example.demo.model.Album;
import com.example.demo.model.Foto;
import com.example.demo.repository.GaleriaRepository;
import com.example.demo.repository.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/galeria")
public class GaleriaController {

    @Autowired private GaleriaRepository albumRepo;
    @Autowired private FotoRepository fotoRepo;

    // Listar Álbuns
    @GetMapping({"", "/listar"})
    public ResponseEntity<?> listarAlbuns() {
        try {
            return ResponseEntity.ok(albumRepo.findAllByOrderByDataCriacaoDesc());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao listar álbuns.");
        }
    }

    // Criar Álbum
    @PostMapping
    public ResponseEntity<?> criarAlbum(@RequestBody Album album) {
        try {
            if (album.getTitulo() == null || album.getTitulo().isBlank()) {
                return ResponseEntity.badRequest().body("Título obrigatório");
            }
            if (album.getDataCriacao() == null) {
                album.setDataCriacao(LocalDate.now());
            }
            return ResponseEntity.ok(albumRepo.save(album));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao salvar álbum");
        }
    }

    // Deletar Álbum
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAlbum(@PathVariable Long id) {
        if (!albumRepo.existsById(id)) return ResponseEntity.notFound().build();
        try {
            albumRepo.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao excluir. Verifique se há fotos.");
        }
    }

    // Listar fotos (Resumo)
    @GetMapping("/{id}/fotos")
    @Transactional(readOnly = true)
    public ResponseEntity<List<FotoResumo>> listarFotos(@PathVariable Long id) {
        return albumRepo.findById(id)
                .map(album -> {
                    List<FotoResumo> resumo = album.getFotos().stream()
                            .map(f -> new FotoResumo(f.getId(), f.getNomeArquivo()))
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(resumo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Upload
    @PostMapping("/{id}/fotos")
    public ResponseEntity<?> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return albumRepo.findById(id).map(album -> {
            try {
                Foto foto = new Foto();
                foto.setNomeArquivo(file.getOriginalFilename());
                foto.setTipoArquivo(file.getContentType());

                String base64 = "data:" + file.getContentType() + ";base64," +
                        Base64.getEncoder().encodeToString(file.getBytes());

                foto.setDadosBase64(base64);
                foto.setAlbum(album);
                fotoRepo.save(foto);
                return ResponseEntity.ok().build();
            } catch (IOException e) {
                return ResponseEntity.badRequest().body("Erro leitura arquivo");
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // Servir Imagem (SRC)
    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> getFotoConteudo(@PathVariable Long id) {
        return fotoRepo.findById(id).map(foto -> {
            try {
                String base64Clean = foto.getDadosBase64().split(",")[1];
                byte[] decodedBytes = Base64.getDecoder().decode(base64Clean);
                return ResponseEntity.ok()
                        .header("Content-Type", foto.getTipoArquivo())
                        .body(decodedBytes);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().<byte[]>build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // Deletar Foto
    @DeleteMapping("/foto/{id}")
    public ResponseEntity<?> deletarFoto(@PathVariable Long id) {
        if(fotoRepo.existsById(id)) {
            fotoRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // DTO
    public static class FotoResumo {
        public Long id;
        public String nome;
        public FotoResumo(Long id, String nome) { this.id = id; this.nome = nome; }
    }
}