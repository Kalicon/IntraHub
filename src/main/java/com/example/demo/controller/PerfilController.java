package com.example.demo.controller;

import com.example.demo.model.Funcionario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.FuncionarioRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private FuncionarioRepository funcRepo;
    @Autowired(required = false) private PasswordEncoder passwordEncoder;

    // 1. CARREGAR DADOS (Necessário para abrir o modal preenchido)
    @GetMapping("/meus-dados")
    public ResponseEntity<?> meusDados() {
        Usuario usuario = getUsuarioLogado();
        if (usuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String foto = (usuario.getFuncionario() != null) ? usuario.getFuncionario().getFoto() : null;
        String nome = (usuario.getFuncionario() != null) ? usuario.getFuncionario().getNome() : usuario.getLogin();

        return ResponseEntity.ok(Map.of(
                "login", usuario.getLogin(),
                "nome", nome,
                "foto", foto != null ? foto : ""
        ));
    }

    // 2. ATUALIZAR (O que já aparece na sua foto)
    @PutMapping
    public ResponseEntity<?> atualizarPerfil(@RequestBody Map<String, String> payload) {
        Usuario usuario = getUsuarioLogado();
        if (usuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            // Senha
            String novaSenha = payload.get("novaSenha");
            if (novaSenha != null && !novaSenha.isBlank()) {
                if (passwordEncoder != null) {
                    usuario.setSenha(passwordEncoder.encode(novaSenha));
                } else {
                    usuario.setSenha("{noop}" + novaSenha);
                }
                usuarioRepo.save(usuario);
            }

            // Foto
            String novaFoto = payload.get("foto");
            if (novaFoto != null && usuario.getFuncionario() != null) {
                Funcionario f = usuario.getFuncionario();
                f.setFoto(novaFoto);
                funcRepo.save(f);
            }

            return ResponseEntity.ok("Perfil atualizado!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar.");
        }
    }

    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        return usuarioRepo.findByLogin(auth.getName());
    }
}