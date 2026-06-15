package com.example.demo.repository;

import com.example.demo.model.EscalaEnfermagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EscalaEnfermagemRepository extends JpaRepository<EscalaEnfermagem, Long> {
    // CORREÇÃO: Busca pelo ID da Escala (e não 'diaId')
    List<EscalaEnfermagem> findByEscalaId(Long escalaId);
}