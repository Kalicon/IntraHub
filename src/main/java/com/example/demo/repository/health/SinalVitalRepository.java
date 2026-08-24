package com.example.demo.repository.health;

import com.example.demo.model.health.SinalVital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SinalVitalRepository extends JpaRepository<SinalVital, Long> {
    List<SinalVital> findByAtendimentoIdOrderByDataRegistroDesc(Long atendimentoId);
    List<SinalVital> findByNivelRiscoNEWS2(String nivelRiscoNEWS2);
}