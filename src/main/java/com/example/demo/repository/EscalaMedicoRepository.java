package com.example.demo.repository;

import com.example.demo.model.EscalaMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EscalaMedicoRepository extends JpaRepository<EscalaMedico, Long> {
    // CORREÇÃO: Busca pelo ID da Escala
    List<EscalaMedico> findByEscalaId(Long escalaId);
}