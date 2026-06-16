package com.example.demo.config;

import com.example.demo.model.Cardapio;
import com.example.demo.model.Funcionario;
import com.example.demo.model.Link;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CardapioRepository;
import com.example.demo.repository.FuncionarioRepository;
import com.example.demo.repository.LinkRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;

@Component
@Configuration
public class SetupDataLoader implements CommandLineRunner {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private FuncionarioRepository funcionarioRepository;
    @Autowired private LinkRepository linkRepository;
    @Autowired private CardapioRepository cardapioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Cria Admin
        if (usuarioRepository.findByLogin("admin") == null) {
            Funcionario adminProfile = new Funcionario();
            adminProfile.setNome("Administrador");
            adminProfile.setCargo("Gestão TI");
            adminProfile.setSetor("TI");
            adminProfile.setRamal("0000");
            adminProfile.setEmail("ti@corphub.com");
            adminProfile.setAtivo(true);
            adminProfile = funcionarioRepository.save(adminProfile);

            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setPapel("ADMIN");
            admin.setAtivo(true);
            admin.setFuncionario(adminProfile);
            usuarioRepository.save(admin);
        }
        // 2. Cria Cardápio Vazio
        if (cardapioRepository.count() == 0) {
            List<String> dias = Arrays.asList("Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado", "Domingo");
            for (String dia : dias) {
                Cardapio c = new Cardapio();
                c.setDiaSemana(dia);
                c.setPratoPrincipal("A definir");
                cardapioRepository.save(c);
            }
        }
    }
}