package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Habilita o processamento das anotações @PreAuthorize nos Controllers
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/auth/**", "/h2-console/**", "/chamados/**", "/manutencao/**", "/reservas/**", "/health/**")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth -> auth
                        // 1. Recursos Estáticos Públicos
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/logo.png").permitAll()

                        // 2. Páginas e fluxos públicos (e console do H2 / Swagger UI / Hospital Health)
                        .requestMatchers("/", "/login", "/auth/**", "/ouvidoria", "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/health", "/health/**", "/health/dashboard", "/health/leitos", "/health/triagem", "/health/sbar", "/health/protocolos", "/health/incidentes").permitAll()
                        .requestMatchers(HttpMethod.POST, "/ouvidoria/api/enviar").permitAll()

                        // 3. Cadastros Públicos (Solicitações via Formulário sem login)
                        .requestMatchers(HttpMethod.POST, "/chamados/**", "/manutencao/**", "/reservas/**", "/api/inscricoes/**").permitAll()

                        // 4. Galeria Social (Leitura pública, escrita restrita)
                        .requestMatchers(HttpMethod.GET, "/galeria/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/galeria/**", "/albuns/**").hasAnyRole("ADMIN", "ESCALA")
                        .requestMatchers(HttpMethod.DELETE, "/galeria/**", "/albuns/**").hasAnyRole("ADMIN", "ESCALA")

                        // 5. Wiki / Documentos (Leitura pública, escrita restrita)
                        .requestMatchers(HttpMethod.GET, "/documentos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/documentos/**").hasAnyRole("ADMIN", "ESCALA")
                        .requestMatchers(HttpMethod.DELETE, "/documentos/**").hasAnyRole("ADMIN", "ESCALA")

                        // 6. Canal de Ética / Ouvidoria (Administração - Apenas ADMIN)
                        .requestMatchers("/ouvidoria/api/listar").hasRole("ADMIN")
                        .requestMatchers("/ouvidoria/api/status/**").hasRole("ADMIN")

                        // 7. Almoxarifado Administrativo (Apenas ADMIN)
                        .requestMatchers("/almoxarifado/api/toggle").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/almoxarifado/api/materiais").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/almoxarifado/api/pedidos/**/concluir").hasRole("ADMIN")

                        // 8. Gestão de Links e Enquetes (Apenas ADMIN)
                        .requestMatchers(HttpMethod.POST, "/links/**", "/enquetes/**").hasRole("ADMIN")

                        // 9. Canais de WhatsApp (Apenas ADMIN ou RH)
                        .requestMatchers(HttpMethod.POST, "/api/whatsapp/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.DELETE, "/api/whatsapp/**").hasAnyRole("ADMIN", "RH")

                        // 10. Plantões e Escalas
                        .requestMatchers(HttpMethod.POST, "/plantoes/**").hasAnyRole("ADMIN", "ESCALA")
                        .requestMatchers(HttpMethod.DELETE, "/plantoes/**").hasAnyRole("ADMIN", "ESCALA")
                        .requestMatchers(HttpMethod.PUT, "/api/escala/**").hasAnyRole("ESCALA", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/escala/replicar/**").hasAnyRole("ESCALA", "ADMIN")

                        // 11. Área Administrativa Geral (ADMIN)
                        .requestMatchers("/admin/**", "/funcionarios/**", "/setores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/salas/**", "/eventos/**", "/avisos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/salas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/inscricoes/evento/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/usuarios/**").hasRole("ADMIN")

                        // 12. Regra Global de Deleção
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")

                        // Qualquer outro request (como visualizar chamados, fazer pedidos de almoxarifado) exige login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}