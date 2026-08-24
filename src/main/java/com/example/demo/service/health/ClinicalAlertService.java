package com.example.demo.service.health;

import com.example.demo.model.health.Atendimento;
import com.example.demo.model.health.ProtocoloClinico;
import com.example.demo.repository.health.AtendimentoRepository;
import com.example.demo.repository.health.ProtocoloClinicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClinicalAlertService {

    @Autowired private ProtocoloClinicoRepository protocoloRepository;
    @Autowired private AtendimentoRepository atendimentoRepository;

    @Transactional
    public ProtocoloClinico abrirProtocolo(Long atendimentoId, String tipoProtocolo, String medico) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId).orElseThrow();

        ProtocoloClinico p = new ProtocoloClinico();
        p.setAtendimento(atendimento);
        p.setTipoProtocolo(tipoProtocolo);
        p.setDataAbertura(LocalDateTime.now());
        p.setStatus("ABERTO");
        p.setMedicoResponsavel(medico);

        switch (tipoProtocolo.toUpperCase()) {
            case "SEPSE": p.setMetaSlaMinutos(60); break;
            case "AVC": p.setMetaSlaMinutos(45); break;
            case "IAM": p.setMetaSlaMinutos(90); break;
            default: p.setMetaSlaMinutos(120); break;
        }

        return protocoloRepository.save(p);
    }

    public List<ProtocoloClinico> listarProtocolosAtivos() {
        return protocoloRepository.findByStatus("ABERTO");
    }

    public List<ProtocoloClinico> listarTodos() {
        return protocoloRepository.findAll();
    }
}