package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.EscalaRepository;
import com.example.demo.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EscalaService {

    private final EscalaRepository escalaRepo;
    private final SetorRepository setorRepo;
    private final AuditService auditService;

    @Autowired
    public EscalaService(EscalaRepository escalaRepo, SetorRepository setorRepo, AuditService auditService) {
        this.escalaRepo = escalaRepo;
        this.setorRepo = setorRepo;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Escala obterPorData(String data) {
        LocalDate dt = LocalDate.parse(data);

        Escala escala = escalaRepo.findByData(dt).orElse(new Escala());
        escala.setData(dt);

        List<Setor> todosSetores = setorRepo.findAll();

        // 1. Verifica Médicos (Cria linhas vazias se faltar setor)
        Map<Long, EscalaMedico> medicosMap = escala.getEscalaMedica().stream()
                .filter(em -> em.getSetor() != null)
                .collect(Collectors.toMap(em -> em.getSetor().getId(), em -> em));

        for (Setor s : todosSetores) {
            if (!medicosMap.containsKey(s.getId())) {
                EscalaMedico novo = new EscalaMedico();
                novo.setSetor(s);
                novo.setEscala(escala);
                escala.getEscalaMedica().add(novo);
            }
        }

        // 2. Verifica Enfermagem (Cria linhas vazias se faltar setor)
        Map<Long, EscalaEnfermagem> enfMap = escala.getEscalaEnfermagem().stream()
                .filter(ee -> ee.getSetor() != null)
                .collect(Collectors.toMap(ee -> ee.getSetor().getId(), ee -> ee));

        for (Setor s : todosSetores) {
            if (!enfMap.containsKey(s.getId())) {
                EscalaEnfermagem novo = new EscalaEnfermagem();
                novo.setSetor(s);
                novo.setEscala(escala);
                escala.getEscalaEnfermagem().add(novo);
            }
        }

        return escala;
    }

    @Transactional
    public Escala salvar(Long id, Escala dadosTela) {
        Escala escalaFinal;
        Optional<Escala> existente = escalaRepo.findByData(dadosTela.getData());

        if (existente.isPresent()) {
            Escala alvo = existente.get();

            // Atualiza Médicos
            alvo.getEscalaMedica().clear();
            if (dadosTela.getEscalaMedica() != null) {
                for (EscalaMedico em : dadosTela.getEscalaMedica()) {
                    if (em.getSetor() != null) {
                        em.setEscala(alvo);
                        alvo.getEscalaMedica().add(em);
                    }
                }
            }

            // Atualiza Enfermagem
            alvo.getEscalaEnfermagem().clear();
            if (dadosTela.getEscalaEnfermagem() != null) {
                for (EscalaEnfermagem ee : dadosTela.getEscalaEnfermagem()) {
                    if (ee.getSetor() != null) {
                        ee.setEscala(alvo);
                        alvo.getEscalaEnfermagem().add(ee);
                    }
                }
            }
            escalaFinal = escalaRepo.save(alvo);
        } else {
            // Nova
            if (dadosTela.getEscalaMedica() != null)
                dadosTela.getEscalaMedica().forEach(em -> em.setEscala(dadosTela));
            if (dadosTela.getEscalaEnfermagem() != null)
                dadosTela.getEscalaEnfermagem().forEach(ee -> ee.setEscala(dadosTela));
            escalaFinal = escalaRepo.save(dadosTela);
        }

        String dataFmt = dadosTela.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        auditService.registrar("ALTERAÇÃO", "ESCALA", "Atualizou a escala de " + dataFmt);

        return escalaFinal;
    }

    @Transactional
    public void replicarEscala(Long id) {
        escalaRepo.findById(id).ifPresent(base -> {
            for (int i = 1; i <= 7; i++) {
                LocalDate novaData = base.getData().plusDays(i);
                if (escalaRepo.findByData(novaData).isEmpty()) {
                    Escala nova = new Escala();
                    nova.setData(novaData);

                    for (EscalaMedico em : base.getEscalaMedica()) {
                        EscalaMedico novoEm = new EscalaMedico();
                        novoEm.setSetor(em.getSetor());
                        novoEm.setMedico(em.getMedico());
                        novoEm.setRamal(em.getRamal());
                        novoEm.setEscala(nova);
                        nova.getEscalaMedica().add(novoEm);
                    }

                    for (EscalaEnfermagem ee : base.getEscalaEnfermagem()) {
                        EscalaEnfermagem novoEe = new EscalaEnfermagem();
                        novoEe.setSetor(ee.getSetor());
                        novoEe.setSupervisor(ee.getSupervisor());
                        novoEe.setRamal(ee.getRamal());
                        novoEe.setEscala(nova);
                        nova.getEscalaEnfermagem().add(novoEe);
                    }
                    escalaRepo.save(nova);
                }
            }
            auditService.registrar("CRIAÇÃO", "ESCALA", "Replicou semana baseada em " + base.getData());
        });
    }
}
