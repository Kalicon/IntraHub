package com.example.demo.controller;

import com.example.demo.model.Funcionario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.FuncionarioRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate; // <--- OBRIGATÓRIO
import com.example.demo.dto.RegistroUsuarioRequestDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private FuncionarioRepository funcionarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistroUsuarioRequestDTO payload) {

        String login = payload.getLogin();
        String senha = payload.getSenha();

        if (usuarioRepository.findByLogin(login) != null) {
            return ResponseEntity.badRequest().body("Usuário já existe.");
        }

        // 1. Cria o Perfil Público (Funcionário)
        Funcionario func = new Funcionario();
        func.setNome(payload.getNome());
        func.setEmail(payload.getEmail());
        func.setRamal(payload.getRamal());
        func.setSetor(payload.getSetor());
        func.setCargo(payload.getCargo());
        func.setFoto(payload.getFoto());

        // --- CORREÇÃO DA DATA (O erro do print) ---
        String dataStr = payload.getDataNascimento();
        if (dataStr != null && !dataStr.isEmpty()) {
            // Converte Texto para Data Real
            func.setDataNascimento(LocalDate.parse(dataStr));
        }
        // ------------------------------------------

        func.setAtivo(true);
        func = funcionarioRepository.save(func);

        // 2. Cria o Login (Usuário)
        Usuario user = new Usuario();
        user.setLogin(login);
        user.setSenha(passwordEncoder.encode(senha));

        // PADRÃO: Entra como USER (Visitante/Leitura)
        // Só vê as coisas, não edita nada até o Admin mudar.
        user.setPapel("USER");
        user.setAtivo(false); // <--- Corrigido para falso, aguardando aprovação do Admin
        user.setFuncionario(func);

        usuarioRepository.save(user);

        return ResponseEntity.ok("Cadastro realizado! Aguarde liberação do Admin.");
    }
}