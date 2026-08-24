package com.example.demo.service.health;

import com.example.demo.model.health.Atendimento;
import com.example.demo.model.health.Paciente;
import com.example.demo.model.health.SinalVital;
import com.example.demo.repository.health.AtendimentoRepository;
import com.example.demo.repository.health.PacienteRepository;
import com.example.demo.repository.health.SinalVitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TriageService {

    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private AtendimentoRepository atendimentoRepository;
    @Autowired private SinalVitalRepository sinalVitalRepository;

    @Transactional
    public Atendimento realizarTriagem(String cpf, String nome, String queixa, String corTriagem, SinalVital sinais) {
        Paciente paciente = pacienteRepository.findByCpf(cpf)
                .orElseGet(() -> {
                    Paciente novo = new Paciente();
                    novo.setCpf(cpf);
                    novo.setNome(nome);
                    return pacienteRepository.save(novo);
                });

        Atendimento atendimento = new Atendimento();
        atendimento.setPaciente(paciente);
        atendimento.setQueixaPrincipal(queixa);
        atendimento.setCorTriagem(corTriagem);
        atendimento.setDataEntrada(LocalDateTime.now());
        atendimento.setStatus("EM_ATENDIMENTO");

        // SLA de Triagem de Manchester em minutos
        switch (corTriagem.toUpperCase()) {
            case "VERMELHO": atendimento.setTempoMaximoMinutosSLA(0); break;
            case "LARANJA": atendimento.setTempoMaximoMinutosSLA(10); break;
            case "AMARELO": atendimento.setTempoMaximoMinutosSLA(60); break;
            case "VERDE": atendimento.setTempoMaximoMinutosSLA(120); break;
            default: atendimento.setTempoMaximoMinutosSLA(240); break; // AZUL
        }

        atendimento = atendimentoRepository.save(atendimento);

        if (sinais != null) {
            sinais.setAtendimento(atendimento);
            sinais.setDataRegistro(LocalDateTime.now());
            sinais.calcularScoreNEWS2();
            sinalVitalRepository.save(sinais);
        }

        return atendimento;
    }

    public List<Atendimento> listarAtendimentosAtivos() {
        return atendimentoRepository.findAll();
    }
}