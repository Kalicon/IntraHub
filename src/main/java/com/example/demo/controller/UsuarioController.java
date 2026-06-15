package com.example.demo.controller;

import com.example.demo.dto.CriarUsuarioRequestDTO;
import com.example.demo.dto.UsuarioResponseDTO;
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/usuarios") // Usando um path mais versionado
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para criar um novo usuário.
     * Recebe os dados do usuário via DTO e utiliza o UsuarioService para a criação.
     *
     * @param request DTO com os dados para a criação do usuário.
     * @return ResponseEntity com status 201 (Created), o location do novo recurso e o corpo do usuário criado (via DTO).
     */
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@RequestBody CriarUsuarioRequestDTO request) throws JsonProcessingException {
        // Delega a lógica de criação para o serviço
        Usuario usuarioSalvo = usuarioService.criarUsuario(request);

        // Constrói a URI do recurso recém-criado (ex: /api/v1/usuarios/1)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioSalvo.getId())
                .toUri();

        // Cria o DTO de resposta para não expor a senha
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(usuarioSalvo);

        // Retorna a resposta HTTP 201 Created
        return ResponseEntity.created(location).body(responseDTO);
    }
}