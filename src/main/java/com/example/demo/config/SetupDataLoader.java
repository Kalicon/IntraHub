package com.example.demo.config;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class SetupDataLoader implements CommandLineRunner {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private FuncionarioRepository funcionarioRepository;
    @Autowired private CardapioRepository cardapioRepository;
    @Autowired private EventoRepository eventoRepository;
    @Autowired private AvisoRepository avisoRepository;
    @Autowired private LinkRepository linkRepository;
    @Autowired private EnqueteRepository enqueteRepository;
    @Autowired private ReservaFrotaRepository frotaRepository;
    @Autowired private AchadoRepository achadoRepository;
    @Autowired private AlbumRepository albumRepository;
    @Autowired private ChamadoTIRepository chamadoTIRepository;
    @Autowired private ManutencaoRepository manutencaoRepository;
    @Autowired private SetorRepository setorRepository;
    @Autowired private MaterialRepository materialRepository;
    @Autowired private com.example.demo.service.LicenseService licenseService;
    @Autowired private com.example.demo.repository.LicencaSistemaRepository licencaRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        // 0. Licenciamento Anual do Sistema
        if (licencaRepository.count() == 0) {
            String chaveMaster = licenseService.gerarChaveAnual("12345678000199", "Hospital e Maternidade Modelo IntraHub", 1);
            licenseService.ativarChave(chaveMaster);
        }
        if (setorRepository.count() == 0) {
            String[] nomesSetores = {"UTI Adulto", "Emergência", "Centro Cirúrgico", "Enfermaria", "Cardiologia", "Recursos Humanos", "Tecnologia da Informação", "Manutenção & Engenharia", "Almoxarifado", "Nutrição & Dietética"};
            for (String s : nomesSetores) {
                Setor set = new Setor();
                set.setNome(s);
                setorRepository.save(set);
            }
        }

        // 2. Criar Admin e Funcionários (com Aniversariantes do Mês)
        if (usuarioRepository.findByLogin("admin") == null) {
            Funcionario adminProfile = new Funcionario();
            adminProfile.setNome("Administrador do Sistema");
            adminProfile.setCargo("Gestão TI & Segurança");
            adminProfile.setSetor("TI");
            adminProfile.setRamal("0000");
            adminProfile.setEmail("admin@intrahub.com");
            adminProfile.setDataNascimento(LocalDate.of(1988, LocalDate.now().getMonthValue(), 10)); // Aniversário este mês!
            adminProfile.setAtivo(true);
            adminProfile = funcionarioRepository.save(adminProfile);

            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setPapel("ADMIN");
            admin.setAtivo(true);
            admin.setFuncionario(adminProfile);
            usuarioRepository.save(admin);
        }

        if (funcionarioRepository.count() <= 1) {
            int mesAtual = LocalDate.now().getMonthValue();

            Funcionario f1 = new Funcionario();
            f1.setNome("Dra. Juliana Costa");
            f1.setCargo("Infectologista Chefe");
            f1.setSetor("UTI Adulto");
            f1.setRamal("1001");
            f1.setEmail("juliana.costa@intrahub.com");
            f1.setDataNascimento(LocalDate.of(1985, mesAtual, 15));
            f1.setAtivo(true);
            funcionarioRepository.save(f1);

            Funcionario f2 = new Funcionario();
            f2.setNome("Dr. Roberto Mendes");
            f2.setCargo("Cardiologista Plantonista");
            f2.setSetor("Cardiologia");
            f2.setRamal("1002");
            f2.setEmail("roberto.mendes@intrahub.com");
            f2.setDataNascimento(LocalDate.of(1982, mesAtual, 22));
            f2.setAtivo(true);
            funcionarioRepository.save(f2);

            Funcionario f3 = new Funcionario();
            f3.setNome("Enf. Carla Santos");
            f3.setCargo("Supervisora de Enfermagem");
            f3.setSetor("Emergência");
            f3.setRamal("2001");
            f3.setEmail("carla.santos@intrahub.com");
            f3.setDataNascimento(LocalDate.of(1990, mesAtual, 5));
            f3.setAtivo(true);
            funcionarioRepository.save(f3);

            Funcionario f4 = new Funcionario();
            f4.setNome("Carlos Oliveira");
            f4.setCargo("Analista de Infraestrutura TI");
            f4.setSetor("Tecnologia da Informação");
            f4.setRamal("4001");
            f4.setEmail("carlos.ti@intrahub.com");
            f4.setDataNascimento(LocalDate.of(1993, 11, 18));
            f4.setAtivo(true);
            funcionarioRepository.save(f4);
        }

        // 3. Cardápio Semanal Rico
        if (cardapioRepository.count() == 0 || cardapioRepository.findAll().stream().anyMatch(c -> "A definir".equals(c.getPratoPrincipal()))) {
            cardapioRepository.deleteAll();

            Cardapio c1 = new Cardapio();
            c1.setDiaSemana("Segunda-feira");
            c1.setData(LocalDate.now().with(java.time.DayOfWeek.MONDAY).toString());
            c1.setPratoPrincipal("Feijoada Light Especial");
            c1.setAcompanhamento("Couve Refogada, Farofa de Banana, Arroz e Pudim de Leite");
            cardapioRepository.save(c1);

            Cardapio c2 = new Cardapio();
            c2.setDiaSemana("Terça-feira");
            c2.setData(LocalDate.now().with(java.time.DayOfWeek.TUESDAY).toString());
            c2.setPratoPrincipal("Escondidinho de Carne Seca");
            c2.setAcompanhamento("Salada Tropical, Purê de Mandioca, Arroz e Mousse de Maracujá");
            cardapioRepository.save(c2);

            Cardapio c3 = new Cardapio();
            c3.setDiaSemana("Quarta-feira");
            c3.setData(LocalDate.now().with(java.time.DayOfWeek.WEDNESDAY).toString());
            c3.setPratoPrincipal("Peixe Assado ao Molho de Ervas");
            c3.setAcompanhamento("Purê de Batata, Legumes Sauté, Arroz Integral e Frutas");
            cardapioRepository.save(c3);

            Cardapio c4 = new Cardapio();
            c4.setDiaSemana("Quinta-feira");
            c4.setData(LocalDate.now().with(java.time.DayOfWeek.THURSDAY).toString());
            c4.setPratoPrincipal("Strogonoff de Frango Crocante");
            c4.setAcompanhamento("Batata Palha, Arroz Branco, Salada Verde e Gelatina");
            cardapioRepository.save(c4);

            Cardapio c5 = new Cardapio();
            c5.setDiaSemana("Sexta-feira");
            c5.setData(LocalDate.now().with(java.time.DayOfWeek.FRIDAY).toString());
            c5.setPratoPrincipal("Lasanha à Bolonhesa Especial");
            c5.setAcompanhamento("Salada Caesar, Arroz de Alho e Pavê de Chocolate");
            cardapioRepository.save(c5);
        }

        // 4. Eventos Corporativos
        if (eventoRepository.count() == 0) {
            Evento e1 = new Evento();
            e1.setTitulo("Workshop: Humanização e Empatia no Atendimento Hospitalar");
            e1.setDataEvento(LocalDate.now().plusDays(5).toString());
            e1.setHoraEvento("14:00");
            e1.setDescricao("Capacitação presencial focada nas melhores práticas de acolhimento de pacientes e familiares.");
            eventoRepository.save(e1);

            Evento e2 = new Evento();
            e2.setTitulo("Treinamento: Protocolo de Sepse & NEWS2 Score");
            e2.setDataEvento(LocalDate.now().plusDays(12).toString());
            e2.setHoraEvento("09:30");
            e2.setDescricao("Treinamento prático para médicos e enfermeiros sobre a nova ferramenta de detecção precoce do IntraHub Health.");
            eventoRepository.save(e2);
        }

        // 5. Avisos Oficiais
        if (avisoRepository.count() == 0) {
            Aviso a1 = new Aviso();
            a1.setTitulo("🚨 Módulo IntraHub Health Ativo: Decisão Clínica e SBAR no ar!");
            a1.setMensagem("Novo sistema de Gestão Hospitalar liberado para triagem de emergência, leitos e passagem de plantão.");
            a1.setDataPostagem(LocalDate.now().toString());
            avisoRepository.save(a1);

            Aviso a2 = new Aviso();
            a2.setTitulo("💉 Campanha de Vacinação contra a Gripe (H1N1) para Colaboradores");
            a2.setMensagem("Vacinação gratuita disponível no Setor de Medicina do Trabalho das 08h às 17h.");
            a2.setDataPostagem(LocalDate.now().toString());
            avisoRepository.save(a2);
        }

        // 6. Links Úteis
        if (linkRepository.count() == 0) {
            Link l1 = new Link();
            l1.setNome("Folha de Pagamento & Holerite");
            l1.setUrl("https://holerite.intrahub.com");
            l1.setClasseIcone("bi bi-file-earmark-pdf-fill text-danger");
            linkRepository.save(l1);

            Link l2 = new Link();
            l2.setNome("Ponto Eletrônico Web");
            l2.setUrl("https://ponto.intrahub.com");
            l2.setClasseIcone("bi bi-clock-history text-primary");
            linkRepository.save(l2);

            Link l3 = new Link();
            l3.setNome("Portal RH / Benefícios");
            l3.setUrl("https://servidor.intrahub.com");
            l3.setClasseIcone("bi bi-person-badge-fill text-success");
            linkRepository.save(l3);

            Link l4 = new Link();
            l4.setNome("Central de Ajuda DTI");
            l4.setUrl("https://suporte.intrahub.com");
            l4.setClasseIcone("bi bi-headset text-warning");
            linkRepository.save(l4);
        }

        // 7. Enquete Ativa
        if (enqueteRepository.count() == 0) {
            Enquete eq = new Enquete();
            eq.setPergunta("Qual melhoria você prefere priorizar no Refeitório Central?");
            eq.setOpcao1("Maior variedade de opções de Saladas e Frutas");
            eq.setOpcao2("Ampliação do horário de atendimento do Almoço");
            eq.setVotos1(18);
            eq.setVotos2(27);
            eq.setAtiva(true);
            enqueteRepository.save(eq);
        }

        // 8. Frota de Veículos
        if (frotaRepository.count() == 0) {
            ReservaFrota r1 = new ReservaFrota();
            r1.setSolicitante("Enf. Carla Santos");
            r1.setVeiculo("Ambulância UTI-01");
            r1.setDestino("Laboratório Central de Análises");
            r1.setDataHoraSaida(LocalDateTime.now().plusHours(2));
            r1.setMotivo("Transporte urgente de amostras biológicas");
            frotaRepository.save(r1);

            ReservaFrota r2 = new ReservaFrota();
            r2.setSolicitante("Carlos Oliveira (TI)");
            r2.setVeiculo("Doblò Operacional 02");
            r2.setDestino("Unidade de Atendimento Leste");
            r2.setDataHoraSaida(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));
            r2.setMotivo("Manutenção de Servidores e Switches");
            frotaRepository.save(r2);
        }

        // 9. Achados e Perdidos
        if (achadoRepository.count() == 0) {
            Achado ac1 = new Achado();
            ac1.setOQue("Crachá Funcional com Cordão Azul");
            ac1.setOnde("Corredor Principal da UTI");
            ac1.setQuemAchou("Maria da Limpeza");
            ac1.setStatus("Aguardando");
            achadoRepository.save(ac1);

            Achado ac2 = new Achado();
            ac2.setOQue("Óculos de Grau com Armação Preta");
            ac2.setOnde("Refeitório Central");
            ac2.setQuemAchou("João do Suporte");
            ac2.setStatus("Aguardando");
            achadoRepository.save(ac2);
        }

        // 10. Álbuns de Fotos / Galeria
        if (albumRepository.count() == 0) {
            Album alb1 = new Album();
            alb1.setTitulo("Inauguração da Nova Ala da UTI Adulto");
            alb1.setDataCriacao(LocalDate.now().minusDays(10));
            alb1.setCapa("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='400' height='250' fill='%230080ff'><rect width='100%' height='100%' fill='%23090e14'/><text x='50%' y='50%' font-size='20' fill='%230080ff' text-anchor='middle'>Nova UTI Adulto</text></svg>");
            albumRepository.save(alb1);

            Album alb2 = new Album();
            alb2.setTitulo("Treinamento de Simulação Realística SBAR");
            alb2.setDataCriacao(LocalDate.now().minusDays(5));
            alb2.setCapa("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='400' height='250' fill='%23f6c23e'><rect width='100%' height='100%' fill='%23090e14'/><text x='50%' y='50%' font-size='20' fill='%23f6c23e' text-anchor='middle'>Treinamento SBAR</text></svg>");
            albumRepository.save(alb2);
        }

        // 11. Chamados TI e Manutenção
        if (chamadoTIRepository.count() == 0) {
            ChamadoTI ch1 = new ChamadoTI();
            ch1.setSolicitante("Dra. Juliana Costa");
            ch1.setRamal("1001");
            ch1.setLocal("Posto de Enfermagem UTI");
            ch1.setTitulo("Ajuste de Impressora de Etiquetas");
            ch1.setDescricao("Impressora térmica desalinhando código de barras de pulseiras.");
            ch1.setPrioridade("Alta");
            ch1.setStatus("Aberto");
            ch1.setDataAbertura(LocalDateTime.now().minusHours(2));
            chamadoTIRepository.save(ch1);
        }

        if (manutencaoRepository.count() == 0) {
            Manutencao m1 = new Manutencao();
            m1.setSolicitante("Enf. Carla Santos");
            m1.setSetor("Emergência");
            m1.setTipo("Elétrica");
            m1.setDescricao("Substituição de lâmpada no leito EME-03");
            m1.setPrioridade("Média");
            m1.setStatus("Aberto");
            m1.setDataAbertura(LocalDateTime.now().minusHours(1));
            manutencaoRepository.save(m1);
        }

        // 12. Materiais no Almoxarifado
        if (materialRepository.count() == 0) {
            Material mat1 = new Material();
            mat1.setNome("Luva Nitrílica Sem Pó (Caixa c/ 100)");
            mat1.setUnidade("Caixa");
            mat1.setDisponivel(true);
            materialRepository.save(mat1);

            Material mat2 = new Material();
            mat2.setNome("Álcool em Gel 70% 500ml");
            mat2.setUnidade("Frasco");
            mat2.setDisponivel(true);
            materialRepository.save(mat2);

            Material mat3 = new Material();
            mat3.setNome("Máscara N95 / PFF2 Respirador");
            mat3.setUnidade("Unidade");
            mat3.setDisponivel(true);
            materialRepository.save(mat3);
        }
    }
}