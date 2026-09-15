# IntraHub — Portal Corporativo e Intranet Integrada

Plataforma corporativa desenvolvida em Java 17 e Spring Boot 3 para centralização de fluxos operacionais, automação de processos internos e gestão com rastreabilidade de conformidade (compliance). O sistema atende a requisitos enterprise como replicação em lote de escalas, controle de concorrência em reservas de recursos, canal confidencial de ouvidoria blindado e trilha estruturada de auditoria.

---

## Demonstração

- **Documentação Interativa da API:** Swagger UI integrado em `/swagger-ui.html`
- **Interface Corporativa:**

```text
[Demonstração visual do painel administrativo, gestão de escalas e central de chamados]
```

---

## Módulos e Funcionalidades

1. **Gestão de Escalas e Plantões:** Organização de plantões por setores operacionais e algoritmo de replicação semanal em lote, permitindo duplicar escalas consolidadas para períodos futuros sem preenchimento manual redundante.
2. **Central de Chamados Técnicos:** Workflow de abertura, triagem, priorização e fechamento de chamados internos de TI e manutenção predial, com gatilhos assíncronos de notificação por e-mail a cada mudança de estado.
3. **Canal de Ética e Ouvidoria Confidencial:** Módulo público para relatos corporativos com suporte a anonimato integral. O acesso aos relatos recebidos é isolado no backend via perfil restrito de administração.
4. **Almoxarifado e Requisição de Materiais:** Catálogo de insumos operacionais e esteira de aprovação com baixa de inventário e controle mestre de operação pelo gestor.
5. **Reserva de Frotas e Salas de Reunião:** Agendamento corporativo com validação temporal para prevenção de conflito de horários (overlap prevention) em veículos e espaços físicos.
6. **Trilha de Auditoria (Compliance):** Interceptador automático de eventos que registra autor, ação, entidade afetada e snapshots dos estados anterior e posterior em formato JSON, com suporte a exportação em CSV para relatórios de controle interno.
7. **Comunicação Institucional:** Mural de avisos internos, galeria de eventos e indexador centralizado de links corporativos.

---

## Tecnologias Utilizadas

### Backend
- **Linguagem e Runtime:** Java 17 (LTS)
- **Framework:** Spring Boot 3.2.3
- **Segurança e Controle de Acesso:** Spring Security 6 (BCrypt, `@EnableMethodSecurity`, RBAC)
- **Persistência de Dados:** Spring Data JPA / Hibernate ORM
- **Bancos de Dados:** H2 Database (desenvolvimento persistido local) e suporte a PostgreSQL
- **Documentação de API:** OpenAPI 3 / Swagger UI (`springdoc-openapi`)
- **Utilitários:** Project Lombok, Apache POI (exportação de relatórios)

### Frontend e Renderização
- **Template Engine:** Thymeleaf (renderização server-side)
- **Estilização e Layout:** Bootstrap 5 (design responsivo com suporte a Dark Mode via localStorage)
- **Visualização Analítica:** Chart.js (gráficos operacionais)
- **Requisições Assíncronas:** JavaScript (Fetch API para operações sem recarga de página)

---

## Decisões de Arquitetura

### 1. Arquitetura em Camadas (Layered Architecture)
O projeto adota o padrão idiomático do ecossistema Spring:
- `Controller`: Endpoints REST e controladores MVC desacoplados da lógica de negócio.
- `Service`: Centralização das regras de negócio, validações transacionais (`@Transactional`) e isolamento de dependências.
- `Repository`: Interfaces de persistência aproveitando o Spring Data JPA para consultas otimizadas.
- `Security`: Camada de interceptação e filtros independentes para autenticação e autorização.

### 2. Controle de Acesso Baseado em Papéis (RBAC)
A segurança é tratada de forma declarativa e no nível de método (`@PreAuthorize`). Cada rota sensível (ex: aprovação de materiais, visualização de denúncias ou registros de auditoria) exige papéis explícitos (`ROLE_ADMIN`, `ROLE_GESTOR`, `ROLE_USER`), impedindo acesso indevido mesmo em casos de chamadas diretas às rotas HTTP.

### 3. Trilha de Auditoria com Snapshots Imutáveis
Modificações críticas no sistema disparam o registro de instâncias de `AuditLog`. Cada registro armazena o identificador do usuário autenticado, timestamp UTC e o estado antes e depois da mutação, assegurando rastreabilidade completa em caso de auditorias internas.

### 4. Resolução de Concorrência em Agendamentos
Para reservas de veículos e salas de reunião, a camada de serviço valida a não existência de sobreposição de intervalos de data/hora (`start < existing.end AND end > existing.start`), garantindo consistência transacional e impedindo agendamentos duplicados do mesmo recurso.

---

## Como Executar Localmente

### Pré-requisitos
- Java Development Kit (JDK) 17 ou superior.
- Git instalado.

### Passo a Passo

1. Clone o repositório:
```bash
git clone https://github.com/Kalicon/IntraHub.git
cd IntraHub
```

2. Execute o projeto usando o Maven Wrapper incluído:

No Linux / macOS:
```bash
./mvnw spring-boot:run
```

No Windows:
```cmd
mvnw.cmd spring-boot:run
```

3. Acesse a aplicação:
- **Painel Web:** `http://localhost:8080`
- **Documentação Swagger / OpenAPI:** `http://localhost:8080/swagger-ui.html`
- **Console H2 (ambiente de desenvolvimento):** `http://localhost:8080/h2-console`

### Usuários Padrão para Testes

O sistema inicializa automaticamente dados de demonstração via `SetupDataLoader`:

| Usuário | Senha | Perfil / Permissão |
| :--- | :--- | :--- |
| `admin` | `admin123` | Acesso total (Administrador do Sistema) |
| `gestor` | `gestor123` | Gestão de equipes e escalas |
| `usuario` | `user123` | Usuário padrão corporativo |

---

## Licença

Este projeto é disponibilizado para fins acadêmicos e de portfólio profissional.
