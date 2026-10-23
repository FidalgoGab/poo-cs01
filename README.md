# FIAP Motiva - Sistema de Monitoramento e Priorização de Roçada de Rodovias

Sprint 4: Migração para Spring Boot, Spring Data JPA e API REST

---

## 1. Visão Geral do Projeto

Este projeto converte o protótipo acadêmico desenvolvido nas sprints anteriores da FIAP Motiva em uma API REST corporativa construida com Spring Boot 3 e Spring Data JPA, conectada ao banco de dados Oracle da FIAP.

### Evolução Arquitetural entre as Sprints

| Sprint       | Escopo e Tecnologias                                                                                        |
| :----------- | :---------------------------------------------------------------------------------------------------------- |
| **Sprint 1** | Domínio base: classes TrechoRodovia, encapsulamento e validações em console.                                |
| **Sprint 2** | Motor de regras: classes abstratas (IntervencaoOperacional), interfaces (MonitoravelViaIoT) e polimorfismo. |
| **Sprint 3** | Persistência relacional: JDBC puro, pattern DAO manual, CRUD no Oracle e scripts SQL.                       |
| **Sprint 4** | Framework corporativo: Spring Boot, Spring Data JPA, DTOs, Bean Validation, ControllerAdvice e API REST.    |

---

## 2. A Grande Virada: De JDBC Puro para Spring Data JPA

Na Sprint 3, cada operação de persistência demandava conexão manual via `DriverManager`, declaração de `PreparedStatement`, tratamento de parâmetros, mapeamento de `ResultSet` e controle manual de fechamento em bloco `finally`.

Na Sprint 4, a infraestrutura de dados foi transferida para o ecossistema Spring:

- **Entidades JPA (`@Entity`):** Mapeiam diretamente as tabelas já existentes no Oracle (`trechos_rodovia`, `equipes_manutencao`, `intervencao_operacional`, `pessoas`, `relatorio_prioridade`).
- **Repositories (`JpaRepository`):** Substituem as classes DAO manuais por interfaces com operações CRUD herdadas e derived queries geradas em runtime.
- **Gerenciamento de Conexão:** Centralizado no pool de conexões do Spring Boot via `application.properties`.
- **Camada de Negócio (`@Service`):** Centraliza o motor de regras de priorização de roçada e as validações de domínio.
- **Camada REST (`@RestController`):** Substitui as impressões no console por endpoints HTTP padronizados com payloads em JSON.

---

## 3. Estrutura do Projeto

O projeto segue a arquitetura em camadas padrão Maven:

```text
poo-cs01/
|-- pom.xml                                           # Configuracão Maven, dependências e plugins
|-- application.properties                            # Configuracões do Oracle e Hibernate
|-- script-criacao.sql                                # DDL do banco Oracle
|-- script-dados.sql                                  # DML de carga inicial de dados
|-- seu-script-consultas.sql                          # Consultas relacionais com junções N:M
|-- src/
    |-- main/
    |   |-- java/
    |   |   `-- br/com/fiapmotiva/
    |   |       |-- MotivaApplication.java            # Ponto de entrada Spring Boot
    |   |       |-- model/                            # Entidades JPA
    |   |       |   |-- TrechoRodovia.java
    |   |       |   |-- EquipeManutencao.java
    |   |       |   |-- IntervencaoOperacional.java
    |   |       |   |-- RelatorioPrioridade.java
    |   |       |   `-- Pessoa.java
    |   |       |-- repository/                       # Interfaces Spring Data JpaRepository
    |   |       |   |-- TrechoRodoviaRepository.java
    |   |       |   |-- EquipeManutencaoRepository.java
    |   |       |   |-- IntervencaoOperacionalRepository.java
    |   |       |   |-- RelatorioPrioridadeRepository.java
    |   |       |   `-- PessoaRepository.java
    |   |       |-- service/                          # Camada de servico e regras de negocio
    |   |       |   |-- TrechoRodoviaService.java
    |   |       |   |-- EquipeManutencaoService.java
    |   |       |   |-- IntervencaoOperacionalService.java
    |   |       |   `-- RelatorioPrioridadeService.java
    |   |       |-- controller/                       # Controladores REST
    |   |       |   |-- TrechoRodoviaController.java
    |   |       |   |-- EquipeManutencaoController.java
    |   |       |   |-- IntervencaoOperacionalController.java
    |   |       |   `-- RelatorioPrioridadeController.java
    |   |       |-- dto/                              # Contratos de requisicao e resposta
    |   |       |   |-- TrechoRequestDTO.java
    |   |       |   |-- TrechoResponseDTO.java
    |   |       |   |-- EquipeRequestDTO.java
    |   |       |   |-- EquipeResponseDTO.java
    |   |       |   |-- IntervencaoRequestDTO.java
    |   |       |   |-- IntervencaoResponseDTO.java
    |   |       |   `-- RelatorioResponseDTO.java
    |   |       `-- exception/                        # Tratamento global de erros
    |   |           |-- GlobalExceptionHandler.java
    |   |           |-- ResourceNotFoundException.java
    |   |           |-- BusinessException.java
    |   |           `-- ErrorResponseDTO.java
    |   `-- resources/
    |       `-- application.properties
    |-- test/
        |-- java/
        |   `-- br/com/fiapmotiva/
        |       |-- MotivaApplicationTests.java
        |       `-- controller/
        |           |-- TrechoRodoviaControllerTest.java
        |           `-- RelatorioPrioridadeControllerTest.java
        `-- resources/
            `-- application.properties                # Configuracao H2 para testes
```

---

## 4. Configuração e Execução

### Pré-requisitos

- Java JDK 17 ou 21
- Apache Maven 3.8+ (ou Maven integrado da IDE)
- Acesso ao banco Oracle FIAP

### Variáveis de Ambiente

As credenciais não ficam em código-fonte. Defina as variáveis de ambiente no sistema ou execute com parâmetros:

- `DB_HOST`: Host do Oracle
- `DB_PORT`: Porta
- `DB_SID`: SID do banco
- `DB_USER`: Usuário
- `DB_PASSWORD`: Senha

O repositório traz um arquivo `.env.example` com a lista de chaves e valores de placeholder (nenhuma credencial real é versionada). Para configurar o ambiente local, copie-o para `.env` e preencha com as suas credenciais do Oracle FIAP:

```bash
# Linux / macOS
cp .env.example .env

# Windows (PowerShell)
Copy-Item .env.example .env
```

Conteúdo esperado do `.env` após o preenchimento:

```properties
DB_HOST=oracle.fiap.com.br
DB_PORT=1521
DB_SID=ORCL
DB_USER=RM000000
DB_PASSWORD=sua_senha_aqui
```

O arquivo `.env` está listado no `.gitignore` e nunca deve ser commitado. As variáveis são lidas pelo Spring em `application.properties` via `${DB_USER:}`, `${DB_PASSWORD:}` etc. Carregue-as na sessão antes de subir a aplicação (por exemplo, exportando-as no shell, usando o suporte a `.env` da IDE, ou passando-as como parâmetros `-DDB_USER=...`).

### Compilação e Testes

Para compilar o projeto e executar a suite de testes automatizados com MockMvc:

```bash
mvn clean test
```

### Execução da Aplicação

```bash
mvn spring-boot:run
```

A API ficara disponivel em: `http://localhost:8080`

---

## 5. Tabela Completa de Endpoints da API REST

### Endpoints de Trechos Rodoviários (`/api/trechos`)

| Método     | Rota                | Descrição                                                         | Status de Sucesso | Status de Erro                  |
| :--------- | :------------------ | :---------------------------------------------------------------- | :---------------- | :------------------------------ |
| **GET**    | `/api/trechos`      | Lista todos os trechos (filtros opcionais `tipo` e `nivelMinimo`) | 200 OK            | 500                             |
| **GET**    | `/api/trechos/{id}` | Busca trecho por ID                                               | 200 OK            | 404 Not Found                   |
| **POST**   | `/api/trechos`      | Cadastra novo trecho rodoviário                                   | 201 Created       | 400 Bad Request                 |
| **PUT**    | `/api/trechos/{id}` | Atualiza dados de um trecho                                       | 200 OK            | 400 Bad Request / 404 Not Found |
| **DELETE** | `/api/trechos/{id}` | Remove um trecho por ID                                           | 204 No Content    | 404 Not Found                   |

### Endpoints de Equipes de Manutencao (`/api/equipes`)

| Método     | Rota                | Descrição                 | Status de Sucesso | Status de Erro                  |
| :--------- | :------------------ | :------------------------ | :---------------- | :------------------------------ |
| **GET**    | `/api/equipes`      | Lista todas as equipes    | 200 OK            | 500                             |
| **GET**    | `/api/equipes/{id}` | Busca equipe por ID       | 200 OK            | 404 Not Found                   |
| **POST**   | `/api/equipes`      | Cadastra nova equipe      | 201 Created       | 400 Bad Request                 |
| **PUT**    | `/api/equipes/{id}` | Atualiza equipe existente | 200 OK            | 400 Bad Request / 404 Not Found |
| **DELETE** | `/api/equipes/{id}` | Remove equipe por ID      | 204 No Content    | 404 Not Found                   |

### Endpoints de Intervenções Operacionais (`/api/intervencoes`)

| Método     | Rota                     | Descrição                           | Status de Sucesso | Status de Erro                  |
| :--------- | :----------------------- | :---------------------------------- | :---------------- | :------------------------------ |
| **GET**    | `/api/intervencoes`      | Lista todos os tipos de intervenção | 200 OK            | 500                             |
| **GET**    | `/api/intervencoes/{id}` | Busca intervenção por ID            | 200 OK            | 404 Not Found                   |
| **POST**   | `/api/intervencoes`      | Cadastra intervenção                | 201 Created       | 400 Bad Request                 |
| **PUT**    | `/api/intervencoes/{id}` | Atualiza intervenção                | 200 OK            | 400 Bad Request / 404 Not Found |
| **DELETE** | `/api/intervencoes/{id}` | Remove intervenção                  | 204 No Content    | 404 Not Found                   |

### Endpoints do Motor de Relatórios de Prioridade (`/api/relatorios`)

| Método   | Rota                                         | Descrição                                                        | Status de Sucesso | Status de Erro  |
| :------- | :------------------------------------------- | :--------------------------------------------------------------- | :---------------- | :-------------- |
| **POST** | `/api/relatorios`                            | Executa o motor sobre os trechos cadastrados e persiste no banco | 201 Created       | 500             |
| **GET**  | `/api/relatorios`                            | Lista o histórico completo de relatórios gerados                 | 200 OK            | 500             |
| **GET**  | `/api/relatorios/periodo?inicio=...&fim=...` | Consulta relatórios por intervalo de datas                       | 200 OK            | 400 Bad Request |

---

## 6. Exemplos de Requisições e Respostas (cURL)

### Requisição 1: Criar Trecho Rodoviário (POST /api/trechos)

Comando:

```bash
curl -X POST http://localhost:8080/api/trechos \
  -H "Content-Type: application/json" \
  -d '{
    "nomeTrecho": "BR-116 KM 60 a 80",
    "kmInicial": 60.0,
    "kmFinal": 80.0,
    "nivelVegetacaoCm": 55.4,
    "tipoTrecho": "UMIDO",
    "monitoravelIot": 1
  }'
```

Resposta (HTTP 201 Created):

```json
{
  "idTrecho": 5,
  "nomeTrecho": "BR-116 KM 60 a 80",
  "kmInicial": 60.0,
  "kmFinal": 80.0,
  "extensaoKm": 20.0,
  "nivelVegetacaoCm": 55.4,
  "tipoTrecho": "UMIDO",
  "monitoravelIot": 1,
  "idEquipe": null,
  "nomeEquipe": null
}
```

### Requisição 2: Listar Trechos com Filtro de Vegetação (GET /api/trechos?nivelMinimo=30.0)

Comando:

```bash
curl -X GET "http://localhost:8080/api/trechos?nivelMinimo=30.0"
```

Resposta (HTTP 200 OK):

```json
[
  {
    "idTrecho": 2,
    "nomeTrecho": "BR-116 - KM 21 a 35",
    "kmInicial": 21.0,
    "kmFinal": 35.0,
    "extensaoKm": 14.0,
    "nivelVegetacaoCm": 35.0,
    "tipoTrecho": "UMIDO",
    "monitoravelIot": 1,
    "idEquipe": 1,
    "nomeEquipe": "Equipe Norte"
  },
  {
    "idTrecho": 3,
    "nomeTrecho": "BR-116 - KM 36 a 48",
    "kmInicial": 36.0,
    "kmFinal": 48.0,
    "extensaoKm": 12.0,
    "nivelVegetacaoCm": 58.75,
    "tipoTrecho": "SECO",
    "monitoravelIot": 0,
    "idEquipe": 1,
    "nomeEquipe": "Equipe Norte"
  }
]
```

### Requisição 3: Validação de Regra de Negócio (POST /api/trechos com kmFinal <= kmInicial)

Comando:

```bash
curl -X POST http://localhost:8080/api/trechos \
  -H "Content-Type: application/json" \
  -d '{
    "nomeTrecho": "Trecho Invalido",
    "kmInicial": 100.0,
    "kmFinal": 50.0,
    "nivelVegetacaoCm": 20.0,
    "tipoTrecho": "SECO"
  }'
```

Resposta (HTTP 400 Bad Request):

```json
{
  "timestamp": "2026-10-19T11:20:00",
  "status": 400,
  "error": "Regra de negocio violada",
  "message": "O quilometro final deve ser estritamente maior que o quilometro inicial.",
  "path": "/api/trechos",
  "details": []
}
```

### Requisição 4: Criar Equipe de Manutenção (POST /api/equipes)

Comando:

```bash
curl -X POST http://localhost:8080/api/equipes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Equipe Leste",
    "descricao": "Responsavel por intervencoes quimicas e rocada no Vale da Ribeira"
  }'
```

Resposta (HTTP 201 Created):

```json
{
  "idEquipe": 3,
  "nome": "Equipe Leste",
  "descricao": "Responsavel por intervencoes quimicas e rocada no Vale da Ribeira"
}
```

### Requisição 5: Gerar Relatório de Prioridade (POST /api/relatorios)

Comando:

```bash
curl -X POST http://localhost:8080/api/relatorios
```

Resposta (HTTP 201 Created):

```json
{
  "idRelatorio": 2,
  "qtUrgente": 2,
  "qtCritico": 1,
  "qtAtencao": 1,
  "qtNormal": 1,
  "totalTrechosAnalisados": 5,
  "resumo": "Trecho [BR-116 - KM 10 a 20] KM 10-20 (22.5 cm): ATENCAO (Monitoramento); Trecho [BR-116 - KM 21 a 35] KM 21-35 (35.0 cm): CRITICO (Pulverizacao Quimica); Trecho [BR-116 - KM 36 a 48] KM 36-48 (58.75 cm): URGENTE (Rocada Mecanizada); ...",
  "dataGeracao": "2026-10-19"
}
```

### Requisição 6: Consultar Relatórios por Período (GET /api/relatorios/periodo)

Comando:

```bash
curl -X GET "http://localhost:8080/api/relatorios/periodo?inicio=2026-01-01&fim=2026-12-31"
```

Resposta (HTTP 200 OK):

```json
[
  {
    "idRelatorio": 2,
    "qtUrgente": 2,
    "qtCritico": 1,
    "qtAtencao": 1,
    "qtNormal": 1,
    "totalTrechosAnalisados": 5,
    "resumo": "Trecho [BR-116 - KM 10 a 20] KM 10-20 (22.5 cm): ATENCAO...",
    "dataGeracao": "2026-10-19"
  }
]
```

### Requisição 7: Remover Trecho (DELETE /api/trechos/{id})

Comando:

```bash
curl -X DELETE http://localhost:8080/api/trechos/5
```

Resposta:
HTTP 204 No Content

---

## 7. Conceitos em Foco (Perguntas de Reflexão da Sprint 4)

### 1. Por que o Repository é uma interface e não uma classe? Quem escreve a implementação e quando?

**Resposta:**
O Repository é declarado como uma interface para desacoplar totalmente o contrato de persistência de qualquer implementação concreta de banco de dados.
Quem escreve a implementação é o próprio framework **Spring Data JPA** em tempo de inicialização da aplicação (**runtime**).
Utilizando o recurso de **Dynamic Proxies** do Java e reflection, o Spring intercepta as chamadas aos métodos da interface `JpaRepository`, traduz a assinatura dos métodos (ou derived queries) em JPQL/SQL nativo e gera dinamicamente uma classe proxy concreta que executa as operações no EntityManager.

### 2. O pattern DAO da Sprint 3 "morreu" na migração ou so mudou de forma? Explique com as suas palavras.

**Resposta:**
O pattern DAO não morreu; ele evoluiu e foi absorvido pelo framework. O papel essencial do pattern DAO sempre foi isolar e encapsular o acesso aos dados da lógica de negócio da aplicação.
Na Sprint 3, esse padrão exigia codificação manual exaustiva para cada operação (abrir conexão, preparar statements, mapear colunas do ResultSet e tratar exceptions).
Na Sprint 4, a interface `JpaRepository` atua exatamente como um DAO fortemente tipado. A diferença é que a implementação repetitiva de baixo nível deixa de ser escrita manualmente e passa a ser gerada automaticamente pelo Spring Data.

### 3. Por que a validação de nivelVegetacao >= 0 (Sprint 1) deve ficar no Service e nao no Controller?

**Resposta:**
O Controller atua na camada Web/Transporte, tendo a responsabilidade exclusiva de receber a requisição HTTP, acionar a validação sintática básica do payload via Bean Validation e retornar a resposta adequada.
A regra de que `nivelVegetacao >= 0` (assim como `kmFinal > kmInicial` e a criticidade para alocação de equipes) é uma regra de negócio fundamental do domínio.
Se essa lógica estivesse no Controller, ela não seria reaproveitada caso a aplicação recebesse dados por outros canais, como mensagens assíncronas de telemetria IoT, tarefas agendadas em lote ou chamadas internas entre serviços. Concentrar essa validação no `@Service` garante que o domínio nunca entre em estado inconsistente, independentemente de qual cliente invoque a operação.

### 4. No JDBC puro voces escreviam SQL. Onde esta o SQL do findByTipo()? Quem o gerou?

**Resposta:**
No JDBC puro, o comando SQL era escrito manualmente como string: `SELECT * FROM trechos_rodovia WHERE tipo_trecho = ?`.
Com o Spring Data JPA, nenhum comando SQL manual precisa ser escrito. O método `findByTipoTrecho(String tipoTrecho)` utiliza o mecanismo de Derived Query Methods.
Durante a inicialização, o S pring Data inspeciona sintaticamente o nome do método:

1. Identifica a ação de busca: `find...By`.
2. Identifica o atributo correspondente na entidade: `tipoTrecho` mapeado na `@Entity TrechoRodovia`.
3. Monta internamente a consulta JPQL correspondente: `SELECT t FROM TrechoRodovia t WHERE t.tipoTrecho = :tipoTrecho`.
4. O Hibernate traduz o JPQL para o dialeto do Oracle (`OracleDialect`), gerando a query SQL nativa otimizada com bind parameters, protegendo a aplicação contra SQL Injection e eliminando código repetitivo.

---

## 8. Integrantes do Grupo

- Gabriel Fidalgo - RM563213
- Gustavo Rossi - RM566075
- Pedro Lima - RM565461
- Gustavo Maia - RM562240
