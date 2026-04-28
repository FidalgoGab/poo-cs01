# CS01_POO

## Visão geral

Este projeto Java demonstra conceitos fundamentais de Programação Orientada a Objetos (POO) através de um exemplo simples de manutenção de rodovias.

### O que está incluído

- `TrechoRodovia`: representa um trecho de rodovia com intervalo de quilômetros e nível de vegetação.
- `Pessoa`: representa um membro de equipe com documento, nome e data de nascimento.
- `EquipeManutencao`: gerencia um grupo de `Pessoa` e os trechos de rodovia associados, com validação para evitar duplicatas.
- `Main`: classe principal que cria objetos, manipula valores e imprime resultados no console.

## Estrutura do projeto

```
src/
  br/com/fiapmotiva/main/Main.java
  br/com/fiapmotiva/model/EquipeManutencao.java
  br/com/fiapmotiva/model/Pessoa.java
  br/com/fiapmotiva/model/TrechoRodovia.java
```

## Como executar

### Usando IntelliJ IDEA

1. Abra o projeto no IntelliJ IDEA.
2. Aguarde a indexação e a sincronização.
3. Execute a classe `br.com.fiapmotiva.main.Main`.

### Usando terminal (javac/java)

1. Navegue até a pasta do projeto:

```bash
cd c:\Users\gabrielfidalgo-mtz\IdeaProjects\cs01_poo
```

2. Compile os arquivos Java:

```bash
javac -d out\production\cs01_poo src\br\com\fiapmotiva\model\*.java src\br\com\fiapmotiva\main\Main.java
```

3. Execute a aplicação:

```bash
java -cp out\production\cs01_poo br.com.fiapmotiva.main.Main
```

## Comportamento esperado

- Cria dois trechos de rodovia e dois membros de equipe.
- Registra o crescimento da vegetação no primeiro trecho.
- Imprime os membros da equipe e trechos cadastrados.
- Lança exceções se membros ou trechos duplicados forem adicionados, ou se valores inválidos forem definidos.

## Aprendizados

- Uso de classes e objetos.
- Encapsulamento com atributos privados e métodos públicos.
- Validação de dados em setters e métodos de adição.
- Listagem de objetos com `toString()`.
