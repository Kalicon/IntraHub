# 🚀 CorpHub - Intranet & Portal Corporativo Unificado

O **CorpHub** é uma solução de intranet moderna, segura e de alto desempenho projetada para centralizar a comunicação interna, agilizar processos operacionais e otimizar a gestão de recursos de uma corporação. 

Desenvolvido com **Java 17**, **Spring Boot 3** e **Thymeleaf**, o projeto serve como um demonstrativo prático de engenharia de software aplicada, cobrindo boas práticas de arquitetura em camadas, segurança corporativa (Spring Security), processamento assíncrono e rastreabilidade por logs de auditoria.

---

## 🛠️ Tecnologias Utilizadas

### **Backend**
* **Linguagem:** Java 17
* **Framework:** Spring Boot 3.2.3
* **Persistência de Dados:** Spring Data JPA (Hibernate)
* **Segurança:** Spring Security 6 (Criptografia BCrypt, Method Security, Proteção CSRF seletiva)
* **Utilitários:** Lombok (redução de boilerplate), Apache POI (exportação e processamento de planilhas)
* **Banco de Dados:** H2 Database (Desenvolvimento / Persistência em arquivo local) | Suporte nativo a PostgreSQL (Produção)

### **Frontend**
* **Template Engine:** Thymeleaf (renderização server-side dinâmica)
* **Estilização:** Bootstrap 5 (CSS responsivo, Dark Mode persistido via `localStorage`, design moderno com Glassmorphism)
* **Gráficos:** Chart.js (painéis estatísticos dinâmicos)
* **Comunicação:** Fetch API (requisições REST assíncronas para manipulação de dados em tempo real)

---

## 🌟 Funcionalidades Principais

* **📅 Gestão de Escalas e Plantões:** Escalas organizadas por setores (equipe de gestão/apoio) com funcionalidade de replicação semanal automatizada em lote.
* **🛠️ Chamados de TI e Manutenção:** Sistema completo de abertura de chamados técnicos com níveis de prioridade e notificações de fluxo via e-mail corporativo.
* **🔒 Canal de Ética e Ouvidoria (Restrito):** Área pública para envio de relatos anônimos ou identificados. Os relatos são protegidos no backend e visíveis unicamente por usuários com perfil `ADMIN`.
* **📦 Almoxarifado Digital:** Catálogo de insumos e fluxo completo de pedidos, permitindo a separação e controle de entrega de materiais com controle operacional ON/OFF do módulo pelo administrador.
* **🚗 Reservas de Frota e Salas:** Agendamento de veículos e salas de reunião corporativas, prevenindo conflitos de datas e horários.
* **🕵️ Trilha de Auditoria (Compliance):** Gravação automática de logs estruturados (`AuditLog`) contendo o autor, a ação, a entidade modificada, o valor antigo e o valor novo em JSON para fins de conformidade regulatória.
* **📢 Mural de Avisos & Links Úteis:** Mural rápido de comunicados e centralizador de links corporativos importantes (como folha de pagamento, ponto, etc.).
* **📷 Galeria Social:** Compartilhamento de álbuns de fotos e eventos internos organizados cronologicamente.
* **📊 Painel de Indicadores:** Gráficos interativos integrados diretamente na Home exibindo volumetria de chamados abertos e concluídos.

---

## 🛡️ Segurança e Arquitetura

O projeto foi refatorado seguindo rigorosos padrões de segurança e arquitetura limpa:
* **Method Security Ativo:** Utilização de `@EnableMethodSecurity` permitindo o uso de `@PreAuthorize("hasRole('ADMIN')")` em métodos específicos do controller para blindagem de acesso a APIs REST.
* **Blindagem de APIs Públicas:** Fechamento da permissão genérica de requisições `GET` públicas. Visitantes sem login acessam unicamente a home e formulários de cadastro ou Ouvidoria. Todo dado interno exige autenticação.
* **Arquitetura Service-Repository:** Desacoplamento total da regra de negócio dos Controllers para classes de Serviço (`@Service`) transacionais.
* **Externalização de Credenciais:** Configurações de senhas de banco de dados e chaves SMTP de e-mail foram externalizadas usando variáveis de ambiente (`${VARIABLE}`) com fallbacks seguros.

---

## 📂 Estrutura de Pacotes

A organização lógica do código segue o padrão idiomático do Spring Boot:

```text
src/main/java/com/example/demo/
├── config/        # Carga inicial de dados (SetupDataLoader) e beans de inicialização
├── controller/    # Endpoints REST e controllers de visualização MVC
├── dto/           # Data Transfer Objects para requisições e respostas de APIs
├── model/         # Entidades de banco de dados (JPA Entities)
├── repository/    # Interfaces de persistência Spring Data JPA
├── scheduler/     # Rotinas e tarefas agendadas (ex: envio de e-mails de eventos às 7h)
├── security/      # Configuração de filtros HTTP, proteção CSRF e permissões
└── service/       # Camada de lógica de negócio e serviços assíncronos (E-mail, Escalas, Auditoria)
```

---

## 🚀 Como Executar o Projeto Localmente

### **Pré-requisitos**
* Java 17 JDK instalado
* Maven 3.x instalado (ou use o Maven Wrapper incluso no projeto)

### **Passos para Execução**

1. Clone este repositório para a sua máquina local:
   ```bash
   git clone https://github.com/Kalicon/intranet-hmlmb.git
   cd intranet-hmlmb
   ```

2. Execute o build e a compilação do projeto para baixar as dependências:
   ```bash
   ./mvnw clean compile
   ```

3. Inicialize a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Acesse a aplicação no seu navegador:
   * **URL do Sistema:** [http://localhost:8080](http://localhost:8080)
   * **Console do Banco H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (Credenciais padrão configuradas no `application.properties`)

### **Usuário Administrador Padrão (Carga Inicial)**
O sistema cria automaticamente um perfil de administrador no primeiro início para testes:
* **Usuário:** `admin`
* **Senha:** `123456`
