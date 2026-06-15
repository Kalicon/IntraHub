package com.example.demo.service;

import com.example.demo.dto.CriarUsuarioRequestDTO;
import com.example.demo.model.AuditLog;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;
    private final ObjectMapper objectMapper; // Para converter objetos em JSON para o log

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuditService auditService, ObjectMapper objectMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param request DTO com os dados do usuário a ser criado.
     * @return O usuário salvo.
     */
    @Transactional
    public Usuario criarUsuario(CriarUsuarioRequestDTO request) throws JsonProcessingException {
        // 1. Hashing da senha - NUNCA salvar a senha em texto puro
        String senhaHasheada = passwordEncoder.encode(request.getSenha());

        // 2. Criação da entidade Usuario
        Usuario novoUsuario = new Usuario();
        novoUsuario.setHospitalId(request.getHospitalId());
        novoUsuario.setLogin(request.getLogin());
        novoUsuario.setSenha(senhaHasheada);
        novoUsuario.setPapel(request.getPapel());
        novoUsuario.setAtivo(true); // Por padrão, criamos o usuário como ativo

        // 3. Persistência no banco de dados
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // 4. Gravação do Log de Auditoria
        // O ID do usuário que realizou a ação viria do contexto de segurança (ex: usuário logado)
        // Por enquanto, vamos passar como nulo.
        Long autorDaAcaoId = null; 
        String valorNovoJson = objectMapper.writeValueAsString(usuarioSalvo);

        AuditLog log = new AuditLog(
            usuarioSalvo.getHospitalId(),
            autorDaAcaoId,
            "CREATE",
            "Usuario",
            usuarioSalvo.getId(),
            null, // Valor antigo é nulo para uma criação
            valorNovoJson
        );
        auditService.gravarLog(log);

        return usuarioSalvo;
    }
}