package com.example.demo.repository.health;

import com.example.demo.model.health.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    List<Atendimento> findByStatus(String status);
    List<Atendimento> findByCorTriagem(String corTriagem);
}