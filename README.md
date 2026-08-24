# 🌐 IntraHub — Portal Corporativo & Intranet Integrada

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange.svg?style=for-the-badge&logo=openjdk" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg?style=for-the-badge&logo=springboot" alt="Spring Boot 3">
  <img src="https://img.shields.io/badge/Spring%20Security-6-blue.svg?style=for-the-badge&logo=springsecurity" alt="Spring Security 6">
  <img src="https://img.shields.io/badge/Frontend-Thymeleaf%20%7C%20Bootstrap%205-blueviolet.svg?style=for-the-badge&logo=bootstrap" alt="Frontend">
  <img src="https://img.shields.io/badge/Database-H2%20%7C%20PostgreSQL-navy.svg?style=for-the-badge&logo=postgresql" alt="Database">
  <img src="https://img.shields.io/badge/Status-Ativo%20%2F%20Portf%C3%B3lio-success.svg?style=for-the-badge" alt="Status">
</p>

---

## 📌 Sobre o Projetos & Propósito

O **IntraHub** é uma plataforma corporativa web completa, moderna e de alta performance, desenvolvida para centralizar a comunicação interna, automatizar processos operacionais e garantir total rastreabilidade sobre a gestão de recursos empresariais.

Este projeto foi estruturado e desenvolvido como um **demonstrativo de Engenharia de Software e Arquitetura de Sistemas**, servindo como **portfólio acadêmico e profissional**. Ele abrange desde regras de negócio complexas (como replicação automatizada de escalas e concorrência em agendamentos) até rigorosos padrões de **segurança corporativa**, **auditoria em conformidade (compliance)** e **interface moderna server-side (Thymeleaf + Bootstrap 5)**.

---

## 🛠️ Arquitetura & Tecnologias

### **Backend**
* **Linguagem & Runtime:** Java 17 (LTS)
* **Framework Principal:** Spring Boot 3.2.3
* **Segurança:** Spring Security 6 (Criptografia BCrypt, `@EnableMethodSecurity`, Proteção CSRF seletiva e RBAC)
* **Persistência de Dados:** Spring Data JPA / Hibernate ORM
* **Banco de Dados:** H2 Database (desenvolvimento com arquivo persistente local `./data/intrahubdb`) | Suporte nativo a PostgreSQL
* **Utilitários Corporativos:** Lombok (redução de boilerplate), Apache POI (exportação e relatórios Excel)

### **Frontend & Interface**
* **Template Engine:** Thymeleaf (renderização dinâmica server-side)
* **Design & Estilização:** Bootstrap 5 com suporte a **Glassmorphism**, **Dark Mode** persistido via `localStorage` e layout 100% responsivo
* **Visualização de Dados:** Chart.js (dashboards estatísticos e indicadores interativos)
* **Interatividade Assíncrona:** JavaScript (Fetch API para requisições REST sem recarregamento de página)

---

## 🌟 Módulos & Funcionalidades Principais

O **IntraHub** é composto por uma suíte completa de módulos operacionais:

### 1. 📅 **Gestão de Escalas e Plantões Corporativos**
- Organização dinâmica de escalas por setor (Equipes de Gestão, Apoio e Enfermagem).
- **Replicação Semanal em Lote:** Algoritmo automatizado que duplica escalas base para semanas subsequentes, otimizando o tempo operacional.

### 2. 🛠️ **Central de Chamados (TI e Manutenção)**
- Abertura, priorização e acompanhamento do fluxo de atendimento de incidentes técnicos.
- Notificações automáticas por e-mail corporativo em cada transição de status do chamado.

### 3. 🔒 **Canal de Ética & Ouvidoria (Restrito & Anônimo)**
- Canal público para envio de relatos corporativos identificados ou 100% anônimos.
- **Blindagem no Backend:** Os relatos recebidos são visíveis unicamente por usuários autorizados com perfil `ADMIN`.

### 4. 📦 **Almoxarifado Digital**
- Catálogo de insumos e fluxo completo de solicitação, aprovação e entrega de materiais.
- Controle operacional pelo administrador com opção de ativar/desativar a operação do módulo em tempo real.

### 5. 🚗 **Reserva de Frota & Salas de Reunião**
- Sistema de agendamento de veículos e salas corporativas com validação para prevenção de conflito de horários.

### 6. 🕵️ **Trilha de Auditoria (Compliance & Conformidade)**
- Registro automático de logs estruturados (`AuditLog`) contendo: **autor**, **ação**, **entidade afetada**, **snapshot do estado antigo** e **estado novo em JSON**.
- Exportação de relatórios em formato CSV para auditoria e controle de conformidade.

### 7. 📢 **Mural de Avisos, Galeria Social & Links Úteis**
- Mural de comunicados internos com priorização, galeria de eventos com upload de fotos e centralizador de links corporativos (ponto, holerite, sistemas externos).

---

## 📐 Estrutura do Projeto

O código-fonte segue a arquitetura em camadas idiomática do Spring Boot (Controller-Service-Repository-Model):

```text
IntraHub/
├── src/main/java/com/example/demo/
│   ├── config/        # SetupDataLoader (Carga inicial) e configurações de Beans
│   ├── controller/    # Endpoints MVC e Controllers REST
│   ├── dto/           # Data Transfer Objects
│   ├── model/         # Entidades JPA (Entities)
│   ├── repository/    # Interfaces Spring Data JPA
│   ├── scheduler/     # Rotinas agendadas (Cron jobs e envio de e-mails)
│   ├── security/      # Spring Security Filter Chain e regras de acesso
│   └── service/       # Camada de Regras de Negócio e Serviços Transacionais
├── src/main/resources/
│   ├── static/        # CSS, JavaScript e imagens estáticas
│   ├── templates/     # Páginas HTML renderizadas pelo Thymeleaf
│   └── application.properties # Configurações da aplicação e fallback de variáveis
├── .agents/           # Configurações do ecossistema de agentes e MCP (Git MCP Server)
├── mvnw.cmd / mvnw    # Maven Wrapper com autoditecção de JDK 17
└── pom.xml            # Gerenciador de dependências Maven
```

---

## 🛡️ Modelo de Segurança (Spring Security 6)

O **IntraHub** aplica o princípio do menor privilégio (*Least Privilege*):

| Nível de Acesso | Permissões |
| :--- | :--- |
| **Público / Visitante** | Acesso à Home, formulário de Ouvidoria, galeria (leitura) e abertura de chamados simples. |
| **USUÁRIO Autenticado** | Solicitação de materiais, reserva de frota/salas e visualização de avisos/cardápio. |
| **ESCALA / RH** | Edição de escalas, gestão de plantões e comunicados específicos. |
| **ADMIN** | Acesso total: gestão de usuários, ouvidoria restrita, controle do almoxarifado, trilha de auditoria e exclusões. |

---

## 🚀 Como Executar o Projetos Localmente

### **Pré-requisitos**
* **Java 17 JDK** (ou superior) instalado na máquina.
* **Git** para clonar o repositório.

### **Passos para Inicialização**

1. **Clonar o Repositório**:
   ```bash
   git clone https://github.com/Kalicon/IntraHub.git
   cd IntraHub
   ```

2. **Compilar e Baixar Dependências**:
   No Windows:
   ```cmd
   .\mvnw.cmd clean compile
   ```
   No Linux / macOS:
   ```bash
   ./mvnw clean compile
   ```

3. **Iniciar a Aplicação**:
   No Windows:
   ```cmd
   .\mvnw.cmd spring-boot:run
   ```
   No Linux / macOS:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acessar no Navegador**:
   - **URL Principal:** [http://localhost:8080](http://localhost:8080)
   - **Console do Banco H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:file:./data/intrahubdb`, Usuário: `sa`, Senha: `password`)

---

### 🔑 Credenciais do Administrador Padrão (Carga Inicial)

Na primeira inicialização, o sistema popula automaticamente o banco de dados com a conta administrativa para testes:

* **Usuário:** `admin`
* **Senha:** `123456`

---

## 📝 Licença & Contribuição

Este projeto está aberto para demonstração, estudos e utilização como portfólio acadêmico e de engenharia de software. Desenvolvido por **Kalicon Amorim**. 

Se você tiver dúvidas, sugestões ou feedbacks sobre a arquitetura do projeto, sinta-se à vontade para abrir uma *Issue* ou enviar um *Pull Request*! 🚀
