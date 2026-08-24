package com.example.demo.repository.health;

import com.example.demo.model.health.PlantaoSBAR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlantaoSBARRepository extends JpaRepository<PlantaoSBAR, Long> {
    List<PlantaoSBAR> findByAtendimentoIdOrderByDataHoraTransfereDesc(Long atendimentoId);
}