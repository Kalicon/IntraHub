package com.example.demo.service.health;

import com.example.demo.model.health.EventoAdverso;
import com.example.demo.repository.health.EventoAdversoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PatientSafetyService {

    @Autowired private EventoAdversoRepository eventoRepository;

    @Transactional
    public EventoAdverso registrarNotificacao(EventoAdverso evento) {
        evento.setDataOcorrencia(LocalDateTime.now());
        evento.setStatusAnalise("PENDENTE");
        return eventoRepository.save(evento);
    }

    public List<EventoAdverso> listarNotificacoes() {
        return eventoRepository.findAll();
    }
}