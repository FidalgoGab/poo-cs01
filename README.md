# CS02_POO

## Visão geral

Este projeto Java demonstra conceitos fundamentais e avançados de Programação Orientada a Objetos (POO) aplicados a um sistema de monitoramento e priorização de roçada de vegetação nas rodovias para a **FIAP Motiva**.

O objetivo da **Sprint 2** foi a implementação de um **Motor de Regras (Inteligência)** capaz de lidar com diferentes comportamentos de crescimento da vegetação (trecho úmido cresce mais rápido que trecho seco) e diferentes tipos de intervenção (mecânica, manual ou química), bem como a integração de monitoramento automático via sensores IoT e a geração automática de um **Relatório de Prioridade**.

---

## Estrutura do Projeto

Abaixo está a estrutura de arquivos atualizada da aplicação:

```text
src/
  br/com/fiapmotiva/main/Main.java              # Classe principal (execução, simulações e testes)
  br/com/fiapmotiva/model/
    - EquipeManutencao.java                     # Gerenciamento de equipe e atribuição de trechos críticos
    - Pessoa.java                               # Representação de um colaborador/membro de equipe
    - TrechoRodovia.java                        # Classe base que representa um trecho de rodovia
    - TrechoSeco.java                           # Subclasse de TrechoRodovia com crescimento padrão
    - TrechoUmido.java                          # Subclasse de TrechoRodovia com crescimento acelerado e IoT
    - MonitoravelViaIoT.java                    # Interface (contrato) para dispositivos/trechos com sensor IoT
    - IntervencaoOperacional.java               # Classe abstrata para serviços de manutenção
    - RocadaMecanizada.java                     # Classe concreta de intervenção para vegetação crítica
    - Pulverizacao.java                         # Classe concreta de intervenção para trechos úmidos críticos
```

---

## O Motor de Regras (Inteligência)

O sistema analisa de forma contínua o estado da vegetação de cada trecho rodoviário, selecionando a ação apropriada baseando-se no nível da vegetação e no tipo de trecho:

1. **Prioridade Alta (Vegetação >= 50.0 cm):** Necessita de intervenção urgente. O algoritmo indica e executa a **Roçada Mecanizada**, que reduz a vegetação para 5.0 cm.
2. **Prioridade Média (Vegetação >= 30.0 cm e < 50.0 cm):**
   - Se o trecho for um **Trecho Úmido** (crescimento rápido), o sistema indica e executa a **Pulverização Química**, reduzindo a vegetação para 8.0 cm.
   - Para outros trechos, indica e executa a **Roçada Manual**, simulando o reset da vegetação para 10.0 cm.
3. **Prioridade Baixa (Vegetação < 30.0 cm):** O nível está sob controle e nenhuma intervenção imediata é necessária.

---

## Classes Abstratas & Polimorfismo

A classe abstrata `IntervencaoOperacional` define o modelo de base e contrato para ações físicas executadas na rodovia. Ela exige que suas classes filhas implementem o método `executarServico(TrechoRodovia trecho)`.

- **Classes Filhas:** `RocadaMecanizada` e `Pulverizacao`.

### Pergunta de Reflexão (Classes Abstratas)

**Por que não faz sentido para a Motiva que uma equipe execute apenas uma "Intervenção Operacional" genérica sem especificar qual é?**

> **Resposta:**
> Porque "Intervenção Operacional" é um conceito abstrato que não possui uma ação física concreta por si só. Uma equipe precisa saber quais recursos levar (máquinas para roçada, produtos para pulverização, etc.). A classe abstrata define o que deve ser feito (`executarServico`), mas as classes filhas concretas definem a maneira.

---

## Interfaces & Desacoplamento

A interface `MonitoravelViaIoT` estabelece um contrato com o método `transmitirDadosSensor()`. Os trechos dotados de tecnologia (ex: `TrechoUmido`) implementam essa interface, permitindo que a aplicação faça a telemetria remota do nível de vegetação.

### Pergunta de Reflexão (Interfaces vs. Herança)

**Qual a diferença arquitetural entre fazer um Trecho herdar de uma classe abstrata vs. implementar uma Interface?**

> **Resposta:**
> A Herança de classe abstrata estabelece uma relação de "é um" e compartilha implementação de código, atributos e estado. Um trecho herda a estrutura básica de `TrechoRodovia`. A Interface estabelece um contrato de comportamento "pode fazer" desacoplado, sem impor uma hierarquia rígida ou compartilhar estrutura. Isso permite que outras entidades (como veículos, drones ou postes de iluminação) também sejam monitoráveis via IoT sem herdar de `TrechoRodovia`.

---

## Como Executar

### 1. Compilação

Navegue até o diretório raiz do projeto e execute:

```bash
javac -d out\production\cs01_poo src\br\com\fiapmotiva\model\*.java src\br\com\fiapmotiva\main\Main.java
```

### 2. Execução da Aplicação

Rode o comando a seguir para iniciar as simulações e visualizar o relatório de prioridades no terminal:

```bash
java -cp out\production\cs01_poo br.com.fiapmotiva.main.Main
```

### 3. Validação dos Testes Unitários Embutidos

Durante a execução da classe `Main`, os seguintes cenários são validados automaticamente via console:

- **Teste do Construtor:** Impede a criação de trecho com KM final menor ou igual ao inicial.
- **Teste de Associação e Criticidade:** Impede a atribuição de equipes a trechos não críticos (vegetação < 30.0 cm).
- **Teste de Abstração:** Garante que a classe base `IntervencaoOperacional` não pode ser instanciada via `new` (tentativa via Reflection resulta em erro de instanciação).
- **Teste de Mock IoT:** Simula a captura de crescimento e transmissão de dados de sensores com um Mock da interface `MonitoravelViaIoT`.
- **Diferenciação de Crescimento:** O trecho úmido cresce com taxa acelerada de 1.5x em relação ao seco.

## Comportamento esperado

- Cria dois trechos de rodovia e dois membros de equipe.
- Registra o crescimento da vegetação no primeiro trecho.
- Imprime os membros da equipe e trechos cadastrados.
- Lança exceções se membros ou trechos duplicados forem adicionados, ou se valores inválidos forem definidos.

## Aprendizados

- Uso de classes e objetos.
- Conceitos de Classes abstratas e Interfaces.
- Encapsulamento com atributos privados e métodos públicos.
- Validação de dados em setters e métodos de adição.
- Listagem de objetos com `toString()`.
