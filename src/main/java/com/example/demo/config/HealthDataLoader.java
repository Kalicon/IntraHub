package com.example.demo.config;

import com.example.demo.model.health.*;
import com.example.demo.repository.health.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class HealthDataLoader implements CommandLineRunner {

    @Autowired private LeitoRepository leitoRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private AtendimentoRepository atendimentoRepository;
    @Autowired private SinalVitalRepository sinalVitalRepository;
    @Autowired private PlantaoSBARRepository sbarRepository;
    @Autowired private ProtocoloClinicoRepository protocoloRepository;
    @Autowired private EventoAdversoRepository eventoRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (leitoRepository.count() == 0) {
            // Seeding Leitos
            String[] utiCodes = {"UTI-01", "UTI-02", "UTI-03", "UTI-04", "UTI-05", "UTI-06"};
            for (String code : utiCodes) {
                Leito l = new Leito();
                l.setCodigo(code);
                l.setSetor("UTI");
                l.setTipo("UTI");
                l.setStatus(code.endsWith("1") || code.endsWith("3") ? "OCUPADO" : "VAGO");
                leitoRepository.save(l);
            }

            String[] emeCodes = {"EME-01", "EME-02", "EME-03", "EME-04", "EME-05"};
            for (String code : emeCodes) {
                Leito l = new Leito();
                l.setCodigo(code);
                l.setSetor("Emergencia");
                l.setTipo("SEMI_INTENSIVA");
                l.setStatus(code.endsWith("2") ? "OCUPADO" : (code.endsWith("4") ? "HIGIENIZACAO" : "VAGO"));
                leitoRepository.save(l);
            }

            String[] enfCodes = {"ENF-101", "ENF-102", "ENF-103", "ENF-104", "ENF-105", "ENF-106", "ENF-107", "ENF-108"};
            for (String code : enfCodes) {
                Leito l = new Leito();
                l.setCodigo(code);
                l.setSetor("Enfermaria");
                l.setTipo("ENFERMARIA");
                l.setStatus(code.endsWith("1") || code.endsWith("5") ? "OCUPADO" : "VAGO");
                leitoRepository.save(l);
            }

            // Seeding Pacientes
            Paciente p1 = new Paciente();
            p1.setNome("Maria Aparecida da Silva");
            p1.setCpf("111.222.333-44");
            p1.setDataNascimento(LocalDate.of(1965, 4, 12));
            p1.setSexo("Feminino");
            p1.setTipoSanguineo("O+");
            p1.setAlergias("Dipirona, Penicilina");
            p1.setAntecedentesMedicos("Hipertensão Arterial, Diabetes Mellitus Tipo 2");
            p1.setConvenio("Bradesco Saúde");
            pacienteRepository.save(p1);

            Paciente p2 = new Paciente();
            p2.setNome("João Carlos de Oliveira");
            p2.setCpf("555.666.777-88");
            p2.setDataNascimento(LocalDate.of(1952, 9, 28));
            p2.setSexo("Masculino");
            p2.setTipoSanguineo("A+");
            p2.setAlergias("Nenhuma alergia conhecida");
            p2.setAntecedentesMedicos("Insuficiência Cardíaca, DPOC");
            p2.setConvenio("Unimed");
            pacienteRepository.save(p2);

            // Seeding Atendimentos
            Atendimento a1 = new Atendimento();
            a1.setPaciente(p1);
            a1.setLeito(leitoRepository.findByCodigo("UTI-01").orElse(null));
            a1.setDataEntrada(LocalDateTime.now().minusHours(12));
            a1.setQueixaPrincipal("Febre alta (39.2°C), prostração, calafrios e taquicardia");
            a1.setCorTriagem("VERMELHO");
            a1.setTempoMaximoMinutosSLA(0);
            a1.setStatus("INTERNADO");
            a1.setMedicoResponsavel("Dra. Helena Castro (Infectologia)");
            a1.setDiagnosticoProvavel("Choque Séptico de Foco Urinário");
            atendimentoRepository.save(a1);

            Atendimento a2 = new Atendimento();
            a2.setPaciente(p2);
            a2.setLeito(leitoRepository.findByCodigo("EME-02").orElse(null));
            a2.setDataEntrada(LocalDateTime.now().minusHours(3));
            a2.setQueixaPrincipal("Dispneia súbita, saturação O2 88% em ar ambiente");
            a2.setCorTriagem("LARANJA");
            a2.setTempoMaximoMinutosSLA(10);
            a2.setStatus("EM_ATENDIMENTO");
            a2.setMedicoResponsavel("Dr. Roberto Mendes (Cardiologia)");
            a2.setDiagnosticoProvavel("Descompensação de Insuficiência Cardíaca");
            atendimentoRepository.save(a2);

            // Sinais Vitais + NEWS2
            SinalVital sv1 = new SinalVital();
            sv1.setAtendimento(a1);
            sv1.setDataRegistro(LocalDateTime.now().minusMinutes(30));
            sv1.setFrequenciaCardiaca(125);
            sv1.setPressaoSistolica(95);
            sv1.setPressaoDiastolica(60);
            sv1.setTemperatura(39.3);
            sv1.setSaturacaoO2(92);
            sv1.setFrequenciaRespiratoria(26);
            sv1.setGlasgow(14);
            sv1.setUsoOxigenioSuplementar(true);
            sv1.calcularScoreNEWS2();
            sinalVitalRepository.save(sv1);

            // SBAR Shift
            PlantaoSBAR sbar1 = new PlantaoSBAR();
            sbar1.setAtendimento(a1);
            sbar1.setDataHoraTransfere(LocalDateTime.now().minusHours(1));
            sbar1.setProfissionalPassando("Enf. Carla Santos");
            sbar1.setProfissionalRecebendo("Enf. Marcos Vinicius");
            sbar1.setTurno("MANHA");
            sbar1.setSituacao("Paciente em suporte vasoativo (Noradrenalina em bomba de infusão).");
            sbar1.setHistorico("Admitida com quadro de Sepse de origem urinária, iniciado Ceftriaxona.");
            sbar1.setAvaliacao("Hipotensa, oligúrica, mantendo febre recorrente.");
            sbar1.setRecomendacao("Coletar novas hemoculturas se pico febril > 38.5°C e balanço hídrico rigoroso.");
            sbar1.setNivelCriticidade("CRITICO");
            sbar1.setPendenciasExames(true);
            sbar1.setOrdemNaoReanimar(false);
            sbarRepository.save(sbar1);

            // Protocolo Sepse
            ProtocoloClinico proto1 = new ProtocoloClinico();
            proto1.setAtendimento(a1);
            proto1.setTipoProtocolo("SEPSE");
            proto1.setDataAbertura(LocalDateTime.now().minusHours(4));
            proto1.setStatus("ABERTO");
            proto1.setMetaSlaMinutos(60);
            proto1.setCondutasAdotadas("Coletado Lactato, Hemoculturas, iniciado Antibioticoterapia na 1ª Hora.");
            proto1.setMedicoResponsavel("Dra. Helena Castro");
            protocoloRepository.save(proto1);

            // Evento Adverso / Segurança do Paciente
            EventoAdverso ev1 = new EventoAdverso();
            ev1.setDataOcorrencia(LocalDateTime.now().minusDays(1));
            ev1.setSetor("UTI");
            ev1.setTipoIncidente("NEAR_MISS");
            ev1.setGravidade("NEAR_MISS");
            ev1.setDescricaoDetalhada("Identificada divergência de dosagem na prescrição de heparina antes da administração. Conferência dupla evitou o erro.");
            ev1.setAcaoImediataTomada("Prescrição corrigida junto ao médico plantonista.");
            ev1.setAnonimo(true);
            ev1.setNotificador("Anônimo");
            ev1.setStatusAnalise("EM_ANALISE");
            eventoRepository.save(ev1);
        }
    }
}