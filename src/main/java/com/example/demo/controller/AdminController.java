package com.example.demo.controller;

import com.example.demo.model.Funcionario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.FuncionarioRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private FuncionarioRepository funcionarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // Lista todos os usuários (incluindo dados do funcionário)
    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Edição Power: Altera Login e Dados do Funcionário de uma vez
    @PutMapping("/usuario/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        return usuarioRepository.findById(id).map(user -> {
            // 1. Atualiza Acesso
            user.setPapel((String) payload.get("papel")); // USER, ADMIN...
            user.setAtivo((Boolean) payload.get("ativo"));

            // Troca de senha opcional
            String novaSenha = (String) payload.get("senha");
            if (novaSenha != null && !novaSenha.trim().isEmpty()) {
                user.setSenha(passwordEncoder.encode(novaSenha));
            }

            // 2. Atualiza Dados Pessoais (Ramal, Setor, etc)
            Funcionario func = user.getFuncionario();
            if (func != null) {
                func.setRamal((String) payload.get("ramal"));
                func.setSetor((String) payload.get("setor"));
                func.setCargo((String) payload.get("cargo"));
                // Garante que o funcionário fique ativo se o usuário estiver ativo
                func.setAtivo((Boolean) payload.get("ativo"));
                funcionarioRepository.save(func);
            }

            usuarioRepository.save(user);
            return ResponseEntity.ok("Usuário atualizado!");
        }).orElse(ResponseEntity.notFound().build());
    }
}