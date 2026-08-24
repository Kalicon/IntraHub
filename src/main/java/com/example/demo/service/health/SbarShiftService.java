package com.example.demo.service.health;

import com.example.demo.model.health.PlantaoSBAR;
import com.example.demo.repository.health.PlantaoSBARRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SbarShiftService {

    @Autowired private PlantaoSBARRepository sbarRepository;

    @Transactional
    public PlantaoSBAR registrarSBAR(PlantaoSBAR sbar) {
        sbar.setDataHoraTransfere(LocalDateTime.now());
        return sbarRepository.save(sbar);
    }

    public List<PlantaoSBAR> listarHistoricoAtendimento(Long atendimentoId) {
        return sbarRepository.findByAtendimentoIdOrderByDataHoraTransfereDesc(atendimentoId);
    }

    public List<PlantaoSBAR> listarTodos() {
        return sbarRepository.findAll();
    }
}