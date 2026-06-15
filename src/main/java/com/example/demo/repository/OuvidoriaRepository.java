package com.example.demo.repository;

import com.example.demo.model.Ouvidoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OuvidoriaRepository extends JpaRepository<Ouvidoria, Long> {
    // Para o painel administrativo (ordem decrescente)
    List<Ouvidoria> findAllByOrderByDataEnvioDesc();
}