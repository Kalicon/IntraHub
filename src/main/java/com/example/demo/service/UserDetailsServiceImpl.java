package com.example.demo.service;

import com.example.demo.model.Usuario;
// IMPORTANTE: Aponta para a pasta nova 'repository'
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário no banco
        Usuario usuario = usuarioRepository.findByLogin(username);

        // Verifica se existe
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        // Verifica se o cadastro foi aprovado pelo Admin
        if (!usuario.isAtivo()) {
            throw new UsernameNotFoundException("Seu cadastro ainda está pendente de aprovação.");
        }

        // Retorna o usuário para o Spring Security logar
        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(usuario.getPapel())
                .build();
    }
}