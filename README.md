# Jogo da Velha — DCC168 Teste de Software (2026-3)

Projeto Maven que abre igual no Eclipse, IntelliJ e VS Code.

## Requisitos

- JDK 17+ para rodar o Maven (o código é compilado para Java 8, por compatibilidade com o Baduíno)
- Maven 3.8+
- Graphviz, para gerar a imagem do grafo de causa-efeito (`dot` no PATH):
  Windows `winget install graphviz` · macOS `brew install graphviz` · Linux `sudo apt install graphviz`

## Abrir na IDE

| IDE      | Como abrir                                                    |
|----------|---------------------------------------------------------------|
| Eclipse  | File → Import → Maven → Existing Maven Projects               |
| IntelliJ | File → Open → selecionar a pasta (ou o `pom.xml`)             |
| VS Code  | Abrir a pasta (extensão *Extension Pack for Java* instalada)  |

## Comandos

```bash
mvn test                                              # compila, formata e roda os testes
mvn fmt:format                                        # só formata o código
mvn exec:java                                         # joga no console
mvn test-compile exec:java@report                     # gera tabelas e grafo do relatório
mvn test-compile org.pitest:pitest-maven:mutationCoverage   # teste de mutação (Parte III)
```

O relatório do PITest sai em `target/pit-reports/index.html`.

## Estrutura

```
src/main/java/br/ufjf/dcc168/tictactoe/
├── domain/    regras do jogo, sem nenhum I/O (alvo do PITest, EclEmma e Baduíno)
├── io/        InputReader / OutputPrinter e implementações de console
├── console/   laço de interação, parser de jogadas e desenho do tabuleiro
└── Main.java  único ponto que conhece System.in / System.out

src/test/java/br/ufjf/dcc168/tictactoe/
├── functional/  TestSet-Func  (Parte I / II-A)    métodos ctNN_...
├── structural/  TestSet-Estr  (Parte II-B)        métodos ceNN_...
├── mutation/    novos casos   (Parte III-B)       métodos cmNN_...
├── suites/      uma suite JUnit por etapa de medição
├── io/          testes da lib de I/O
├── specification/  O QUE O GRUPO ESCREVE: classes de equivalência e grafo de causa-efeito
├── causeeffect/    modelo do grafo, tabela de decisão e exportação para .dot
├── report/         @TestCase e ReportGenerator (gera tabelas, .dot e .png)
└── support/        ScriptedInputReader e RecordingOutputPrinter (dublês de teste)
```

### Qual suite rodar em cada etapa

| Etapa       | Suite                          | Ferramenta              |
|-------------|--------------------------------|-------------------------|
| Parte II-A  | `FunctionalSuite`              | EclEmma + Baduíno       |
| Parte II-B  | `FunctionalAndStructuralSuite` | EclEmma + Baduíno       |
| Parte III-A | `FunctionalAndStructuralSuite` | PITest                  |
| Parte III-B | `AllTestsSuite`                | PITest                  |

## Especificação da Parte I

`docs/parte-1/especificacao-parte-1.md`: decisões do grupo, classes de equivalência, valores
limite, grafo de causa-efeito, tabela de decisão e os 21 casos de teste do TestSet-Func.
O código em `specification/` ainda usa a numeração antiga de exemplo e precisa ser alinhado
a esse documento quando os testes forem implementados.

## Artefatos do relatório (gerados automaticamente)

| Artefato | Fonte | O que o grupo escreve |
|---|---|---|
| Tabela 1 – Classes de Equivalência | `specification/EquivalenceClass` (+ `InputCondition`) | uma constante por classe |
| Grafo de Causa-Efeito (.dot e .png) | `specification/CauseEffectSpecification` | causas, efeitos e ligações |
| Tabela de Decisão | derivada do grafo | nada: sai sozinha |
| Tabela 2 – Casos de Teste | anotação `@TestCase` em cada teste | ID, entrada, saída esperada e classes |

Para gerar: botão direito em `ReportGenerator` → *Run as Java Application* (qualquer IDE)
ou `mvn test-compile exec:java@report`. Tudo sai em `docs/reports/generated/`:

- `relatorio.md`: todas as tabelas e a imagem do grafo, juntas;
- `grafo-causa-efeito.dot` e `grafo-causa-efeito.png`;
- `tabela-*.md`: uma tabela por arquivo.

O gerador avisa no console se o TestSet-Func não exercita alguma classe de equivalência, se há
ID repetido, se algum `@Test` ficou sem `@TestCase` ou se o Graphviz não está instalado.

### Grafo de causa-efeito

O grafo é escrito em código, e o desenho e a tabela de decisão saem dele:

```java
Node rowInRange = graph.cause("C1", "Linha entre 0 e 2");
Node columnInRange = graph.cause("C2", "Coluna entre 0 e 2");
Node validPosition = graph.intermediate("N1", and(rowInRange, columnInRange));
graph.effect("E1", "Rejeita: posição fora do tabuleiro", not(validPosition));
```

No desenho, ∧ / ∨ dentro do nó indicam a porta E / OU, e a aresta tracejada com `~` indica
negação. Na tabela de decisão, cada coluna (R1, R2...) é uma regra, e cada regra deve virar pelo
menos um caso de teste da Tabela 2. `–` significa que o valor da causa não importa naquela regra.

### Tabela 2

A coluna **Saída Obtida** é preenchida rodando os testes: se o teste passou, repete a saída
esperada; se falhou, mostra `FALHOU – <mensagem>`; se está com `@Ignore`, mostra `Não executado`.

```java
@TestCase(
        id = "CT02",
        input = "<X:(0,0), O:(0,0)>",
        expected = "InvalidMoveException (CELL_OCCUPIED)",
        classes = {V1, V2, I5, V4})
@Test
public void ct02_moveOnOccupiedCell_throwsInvalidMove() { ... }
```

## Convenções do time

- **Código em inglês, comentários em português.**
- **Legibilidade primeiro:** nomes descritivos, métodos curtos, nada de abreviação.
- **Formatação não se discute:** o build formata tudo (google-java-format, estilo AOSP, 4 espaços).
  Rodem `mvn fmt:format` antes de cada commit.
- **Nome de teste:** `ct07_moveOnOccupiedCell_throwsInvalidMove` → ID da Tabela 2 + cenário + resultado.
- **Domínio sem I/O:** `Scanner` e `System.out` só existem em `io/` e `Main`.

## Regras definidas pelo grupo (entram na Parte I)

- X sempre começa.
- Linhas e colunas numeradas de 0 a 2.
- Célula ocupada, posição fora do tabuleiro e jogada após o fim → `InvalidMoveException`.
- Vitória é verificada antes do empate (vitória na 9ª jogada é vitória).
