package com.example.demo.service.health;

import com.example.demo.model.health.Atendimento;
import com.example.demo.model.health.Leito;
import com.example.demo.repository.health.AtendimentoRepository;
import com.example.demo.repository.health.LeitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BedManagementService {

    @Autowired private LeitoRepository leitoRepository;
    @Autowired private AtendimentoRepository atendimentoRepository;

    public List<Leito> listarTodosLeitos() {
        return leitoRepository.findAll();
    }

    @Transactional
    public void alocarPaciente(Long atendimentoId, Long leitoId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId).orElseThrow();
        Leito leito = leitoRepository.findById(leitoId).orElseThrow();

        leito.setStatus("OCUPADO");
        leitoRepository.save(leito);

        atendimento.setLeito(leito);
        atendimento.setStatus("INTERNADO");
        atendimentoRepository.save(atendimento);
    }

    @Transactional
    public void darAltaPaciente(Long atendimentoId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId).orElseThrow();
        Leito leito = atendimento.getLeito();

        if (leito != null) {
            leito.setStatus("HIGIENIZACAO");
            leitoRepository.save(leito);
        }

        atendimento.setStatus("ALTA");
        atendimento.setDataAlta(LocalDateTime.now());
        atendimentoRepository.save(atendimento);
    }

    @Transactional
    public void atualizarStatusLeito(Long leitoId, String novoStatus) {
        Leito leito = leitoRepository.findById(leitoId).orElseThrow();
        leito.setStatus(novoStatus);
        leitoRepository.save(leito);
    }
}