# SPEC de implementação – Jogo da Velha (DCC168 Teste de Software)

> Este documento é a especificação completa do que implementar neste repositório. Leia também
> `docs/parte-1/especificacao-parte-1.md` (oráculo de teste). Execute as fases da seção 10 em
> ordem. Não execute as fases da seção 13 sem um pedido explícito.

---

## 1. Contexto

Trabalho final da disciplina DCC168 – Teste de Software (UFJF, 2026-3), em grupo. O trabalho
tem três partes:

| Parte | Conteúdo | Situação |
|---|---|---|
| I – Teste Funcional | Classes de equivalência, valor limite, grafo de causa-efeito e casos de teste (TestSet-Func) | **Pronta**: `docs/parte-1/especificacao-parte-1.md` |
| II – Teste Estrutural | Implementar o jogo em Java; automatizar o TestSet-Func em JUnit; medir cobertura (EclEmma e Baduíno); criar o TestSet-Estr até 100% | **Esta SPEC cobre a II-A** |
| III – Teste de Mutação | PITest com todos os operadores; matar mutantes; explicar equivalentes | Fora desta SPEC (seção 13) |

O programa testado é um Jogo da Velha de console para dois jogadores humanos.

## 2. Objetivo desta SPEC

Ao final da implementação, o repositório deve ter:

1. o jogo completo e jogável no console;
2. os 21 casos de teste do TestSet-Func (CT01–CT21) automatizados em JUnit 4, rastreáveis à
   especificação da Parte I;
3. o gerador de relatório (`ReportGenerator`) produzindo Tabela 1, Tabela 2, grafo de
   causa-efeito e tabela de decisão iguais aos da especificação da Parte I;
4. build pronto para as Partes II-B e III: cobertura por linha de comando (JaCoCo) e PITest
   configurável por etapa;
5. registro dos defeitos encontrados na primeira execução do TestSet-Func.

## 3. Escopo

### Dentro do escopo

- Implementar `Position`, `Board`, `Game`, `GameStatus` e `InvalidMoveException` (domínio).
- Ajustar `ConsoleGame` (fim da entrada e mensagens).
- Alinhar `specification/EquivalenceClass` e `specification/InputCondition` à Tabela 1.
- Escrever `GameFunctionalTest` e `ConsoleFunctionalTest` (CT01–CT21) e o apoio `support/Moves`.
- Ajustar `ReportGenerator`, suites, `pom.xml` e `README.md`.
- Criar `docs/defeitos.md`.

### Fora do escopo

- Interface gráfica, jogador automático (IA), tabuleiro de outro tamanho, desfazer jogada.
- TestSet-Estr (Parte II-B) e testes de mutação (Parte III-B): seção 13.
- Tudo o que depende do Eclipse: relatórios do EclEmma e do Baduíno, screenshots da view PIT
  Summary (Pitclipse), geração de `.project`/`.classpath`, zip de entrega.
- Alterar as decisões ou os casos de teste da Parte I.

## 4. Restrições técnicas

| Item | Regra |
|---|---|
| Linguagem | Java, **compatível com Java 8** (sintaxe e API). O Baduíno exige bytecode e API do Java 8 |
| Proibido (Java 9+) | `var`, `List.of`/`Set.of`/`Map.of`, `record`, `switch` com `->`, text blocks, `String.isBlank/strip/repeat/lines`, `Optional.isEmpty`, `Stream.toList()` |
| Build | Maven 3.8+; o Maven roda com JDK 17+ (exigência do formatter); o bytecode é Java 8 |
| Testes | JUnit **4.13.2** (inclui `Assert.assertThrows`). Não usar JUnit 5 |
| Dependências | Não adicionar dependências de produção. Plugins de build só os listados na seção 12 |
| Formatação | google-java-format, estilo AOSP (4 espaços), aplicado pelo build (`mvn fmt:format`) |
| Idioma | Código (classes, métodos, variáveis) em **inglês**; comentários e javadoc em **português**; mensagens ao usuário em português |
| Prioridade | **Legibilidade** acima de concisão ou desempenho |

## 5. Estado atual do repositório

| Caminho | Situação | O que fazer |
|---|---|---|
| `domain/Symbol.java` | Pronto | Nada |
| `domain/GameStatus.java` | Pronto | Adicionar `victoryOf` (7.2) |
| `domain/InvalidMoveException.java` | Pronto, assinatura antiga | Mudar construtor (7.3) |
| `domain/Position.java` | Esqueleto (TODO) | Implementar (7.4) |
| `domain/Board.java` | Esqueleto (TODO) | Implementar (7.5) |
| `domain/Game.java` | Esqueleto (TODO) | Implementar (7.6) |
| `io/*` | Pronto e testado | Nada |
| `console/BoardRenderer.java` | Pronto | Nada |
| `console/MoveParser.java`, `InvalidInputException.java` | Pronto | Nada (conferir mensagens da 8.4) |
| `console/ConsoleGame.java` | Quase pronto | Tratar fim da entrada (8.3) |
| `Main.java` | Pronto | Nada |
| `test/.../causeeffect/*` | Pronto e testado | **Não alterar** |
| `test/.../specification/CauseEffectSpecification.java` | Pronto, igual à Parte I | **Não alterar** |
| `test/.../specification/EquivalenceClass.java`, `InputCondition.java` | Exemplos antigos | Reescrever (9.2) |
| `test/.../report/*` | Pronto e testado | Ajustar `ReportGenerator` (9.6) |
| `test/.../functional/GameFunctionalTest.java` | 2 exemplos com `@Ignore` | Reescrever (9.3) |
| `test/.../structural/`, `mutation/` | Classes vazias com `@Ignore` | Nada (seção 13) |
| `test/.../suites/*` | Pronto | Incluir `ConsoleFunctionalTest` (9.5) |
| `test/.../support/*` | Pronto | Adicionar `Moves` (9.4) |
| `pom.xml` | Pronto | Ajustes da seção 12 |

## 6. Arquitetura

### 6.1 Camadas

```
┌──────────────────────────────────────────────────────────────┐
│ Main                     monta tudo com o console real       │
└──────────────┬───────────────────────────────────────────────┘
               │
┌──────────────▼───────────────────────────────────────────────┐
│ console/                 interação com o jogador             │
│   ConsoleGame    laço: pede jogada → aplica → mostra estado  │
│   MoveParser     texto "1 2" → Position                      │
│   BoardRenderer  desenha o tabuleiro                         │
└───────┬──────────────────────────────────────┬───────────────┘
        │ usa                                  │ usa
┌───────▼──────────────────┐   ┌───────────────▼───────────────┐
│ io/                      │   │ domain/   regras, sem I/O     │
│   InputReader            │   │   Game      turnos e resultado│
│   OutputPrinter          │   │   Board     células e linhas  │
│   Console* (System.*)    │   │   Position  coordenada válida │
└──────────────────────────┘   │   Symbol, GameStatus,         │
                               │   InvalidMoveException        │
                               └───────────────────────────────┘
```

### 6.2 Regras de dependência

- `domain` não depende de nenhum outro pacote do projeto e não faz I/O (`System.in`,
  `System.out`, `Scanner` são proibidos ali).
- `io` não depende de nenhum outro pacote do projeto.
- `console` depende de `domain` e `io`.
- `Main` é o único lugar que instancia `ConsoleInputReader` e `ConsoleOutputPrinter`.
- Testes de `functional/` usam `domain`, `console`, `io` e `support`; nunca `System.in/out`.

### 6.3 Fluxo de uma jogada

```
ConsoleGame        MoveParser        Position          Game              Board
    │ readLine()       │                 │                │                 │
    │──"1 2"──────────▶│                 │                │                 │
    │                  │ new Position(1,2)                │                 │
    │                  │────────────────▶│ valida 0..2    │                 │
    │◀─────────────────┴─────Position────┘ (ou lança POSITION_OUT_OF_BOUNDS)│
    │ play(position) ──────────────────────────────────▶ │                 │
    │                                                    │ partida acabou? │
    │                                                    │ (GAME_ALREADY_FINISHED)
    │                                                    │ place(símbolo) ▶│ ocupada?
    │                                                    │                 │ (CELL_OCCUPIED)
    │                                                    │ hasCompleteLine?│
    │                                                    │ isFull?         │
    │                                                    │ status / turno  │
    │ imprime tabuleiro, resultado ou "Jogada inválida: <motivo>"          │
```

A ordem das verificações (posição → partida em andamento → célula vazia → vitória → empate)
é a decisão **D8/D9** da Parte I e é exigida pelos casos CT06, CT15 e CT16.

### 6.4 Estrutura final de pastas

```
src/main/java/br/ufjf/dcc168/tictactoe/
├── Main.java
├── domain/   Symbol, GameStatus, Position, Board, Game
│   └── exception/  InvalidMoveException
├── io/       InputReader, OutputPrinter, ConsoleInputReader, ConsoleOutputPrinter
│   └── exception/  InputExhaustedException
└── console/  ConsoleGame, MoveParser, BoardRenderer
    └── exception/  InvalidInputException

src/test/java/br/ufjf/dcc168/tictactoe/
├── functional/     GameFunctionalTest, ConsoleFunctionalTest   ← NOVO / REESCRITO
├── structural/     GameStructuralTest (vazio, Parte II-B)
├── mutation/       GameMutationTest (vazio, Parte III-B)
├── suites/         FunctionalSuite, FunctionalAndStructuralSuite, AllTestsSuite
├── specification/  EquivalenceClass, InputCondition (REESCRITOS), CauseEffectSpecification
├── causeeffect/    modelo do grafo (não alterar)
├── report/         TestCase, ReportGenerator, ... 
├── io/             testes da lib de I/O
└── support/        ScriptedInputReader, RecordingOutputPrinter, Moves   ← NOVO

docs/
├── SPEC.md                         este documento
├── defeitos.md                     ← NOVO (seção 11)
└── parte-1/especificacao-parte-1.md
```

---

## 7. Especificação do domínio

Princípio geral: **nenhum código defensivo além do especificado** (sem checagem de `null`,
sem validações extras). Cada ramo a mais precisa de caso de teste na Parte II-B e gera mutantes
na Parte III. Pré-condição documentada: nenhum argumento é `null`.

### 7.1 `Symbol` (pronto)

`enum { X, O }`, com `Symbol opponent()`.

### 7.2 `GameStatus`

`enum { IN_PROGRESS, X_WINS, O_WINS, DRAW }`, com:

| Método | Contrato |
|---|---|
| `boolean isFinished()` | Já existe: `true` para tudo exceto `IN_PROGRESS` |
| `static GameStatus victoryOf(Symbol winner)` | **Novo**: `X → X_WINS`, `O → O_WINS` |

### 7.3 `InvalidMoveException`

`RuntimeException` com o motivo da rejeição. Cada motivo tem uma mensagem fixa em português,
que o console exibe.

```java
public enum Reason {
    POSITION_OUT_OF_BOUNDS("posição fora do tabuleiro (use valores de 0 a 2)"),
    CELL_OCCUPIED("célula já ocupada"),
    GAME_ALREADY_FINISHED("a partida já terminou");
    // getMessage()
}

public InvalidMoveException(Reason reason)   // super(reason.getMessage())
public Reason getReason()
```

O construtor antigo `(Reason, String)` deve ser removido.

### 7.4 `Position`

Objeto de valor imutável: uma coordenada **válida** do tabuleiro.

| Membro | Contrato |
|---|---|
| `MIN_INDEX = 0`, `MAX_INDEX = 2` | Constantes já existentes |
| `Position(int row, int column)` | Se `row` ou `column` estiver fora de `[0, 2]`, lança `InvalidMoveException(POSITION_OUT_OF_BOUNDS)`. Senão, guarda os valores |
| `getRow()`, `getColumn()` | Getters |
| `toString()` | `"(row, column)"`, já existe |

**Não** implementar `equals`/`hashCode` (remover o TODO): nada no projeto compara posições, e
cada método a mais exige testes e gera mutantes.

### 7.5 `Board`

Tabuleiro 3x3. Guarda símbolos e detecta linhas completas. Não conhece turnos.

| Membro | Contrato |
|---|---|
| `SIZE = 3` | Já existe |
| `Symbol getSymbolAt(Position p)` | Símbolo da célula, ou `null` se vazia. Já existe |
| `boolean isEmptyAt(Position p)` | Já existe |
| `void place(Symbol s, Position p)` | Se a célula estiver ocupada, lança `InvalidMoveException(CELL_OCCUPIED)` sem alterar nada. Senão, grava `s` |
| `int countFilledCells()` | **Novo**: quantidade de células preenchidas (0 a 9) |
| `boolean isFull()` | `countFilledCells() == SIZE * SIZE` |
| `boolean hasCompleteLine(Symbol s)` | `true` se alguma das 8 linhas vencedoras (3 linhas, 3 colunas, 2 diagonais) tem as 3 células com `s` |

Implementação recomendada para `hasCompleteLine`: uma **tabela constante com as 8 linhas
vencedoras** (cada uma com 3 coordenadas) e um laço que verifica cada linha. Evitar 8 `if`
encadeados: a tabela é mais legível e gera menos ramos.

### 7.6 `Game`

Partida: controla turnos e decide o resultado.

| Membro | Contrato |
|---|---|
| `Game()` | Tabuleiro vazio, `currentPlayer = X` (D1), `status = IN_PROGRESS` |
| `void play(Position p)` | Algoritmo abaixo |
| `Symbol getCurrentPlayer()` | Jogador da vez. Depois do fim da partida, continua sendo quem fez a última jogada |
| `GameStatus getStatus()` | Estado atual |
| `Board getBoard()` | Tabuleiro (para desenho e asserções) |

Algoritmo de `play(position)`, nesta ordem:

1. Se `status.isFinished()`: lança `InvalidMoveException(GAME_ALREADY_FINISHED)`.
2. `board.place(currentPlayer, position)` (pode lançar `CELL_OCCUPIED`).
3. Se `board.hasCompleteLine(currentPlayer)`: `status = GameStatus.victoryOf(currentPlayer)`.
4. Senão, se `board.isFull()`: `status = DRAW`.
5. Senão: `currentPlayer = currentPlayer.opponent()`.

Invariantes:

- Jogada rejeitada (qualquer `InvalidMoveException`) **não altera** tabuleiro, status nem jogador
  da vez (D4–D7).
- A vitória é verificada **antes** do empate (D9): completar uma linha na 9ª jogada é vitória.
- A validação da posição acontece em `new Position(...)`, antes de `play`. Por isso uma posição
  inválida é rejeitada como `POSITION_OUT_OF_BOUNDS` mesmo com a partida encerrada (D8, CT16).

---

## 8. Especificação do console

### 8.1 `MoveParser` (pronto, conferir)

`static Position parse(String input)`:

1. `input.trim().split("\\s+")`.
2. Se não houver exatamente 2 partes: `InvalidInputException("informe exatamente dois números")`.
   Isso inclui a linha vazia (CT20), um valor (CT18) e três valores (CT19).
3. Cada parte é convertida com `Integer.parseInt`; se falhar:
   `InvalidInputException("'<parte>' não é um número")`. A primeira parte inválida é a
   reportada. Inteiros grandes demais para `int` também caem aqui.
4. Retorna `new Position(linha, coluna)` (pode lançar `POSITION_OUT_OF_BOUNDS`).

Espaços extras no início, no fim ou entre os números são aceitos (CT21).

### 8.2 `BoardRenderer` (pronto)

Sem alteração.

### 8.3 `ConsoleGame`

Construtor: `ConsoleGame(Game game, InputReader reader, OutputPrinter printer)` (já existe).

`void run()`:

1. Imprime o título.
2. Enquanto a partida não terminou: desenha o tabuleiro, imprime o pedido de jogada, lê uma
   linha, converte com `MoveParser.parse` e chama `game.play`. Se `InvalidInputException` ou
   `InvalidMoveException`: imprime `Jogada inválida: <mensagem da exceção>` e repete **sem**
   passar a vez.
3. Ao terminar: desenha o tabuleiro final e imprime a mensagem de resultado.
4. **Novo:** se o leitor lançar `InputExhaustedException` em qualquer momento, imprime a mensagem
   de fim de entrada e retorna normalmente (sem propagar a exceção). Isso encerra o jogo com
   Ctrl+D/Ctrl+Z no console e permite testar entradas que não terminam a partida (CT17–CT21).

### 8.4 Mensagens (texto exato)

| Situação | Texto |
|---|---|
| Início | `Jogo da Velha` |
| Pedido de jogada | `Vez de X. Digite linha e coluna (ex.: 0 2):` (com `O` na vez de O) |
| Quantidade de valores ≠ 2 | `Jogada inválida: informe exatamente dois números` |
| Valor não inteiro | `Jogada inválida: 'a' não é um número` (com o valor digitado) |
| Fora do tabuleiro | `Jogada inválida: posição fora do tabuleiro (use valores de 0 a 2)` |
| Célula ocupada | `Jogada inválida: célula já ocupada` |
| Partida encerrada | `Jogada inválida: a partida já terminou` (não ocorre pelo console, porque o laço termina antes) |
| Vitória | `X venceu!` ou `O venceu!` |
| Empate | `Empate!` |
| Fim da entrada | `Entrada encerrada. Partida interrompida.` |

### 8.5 `Main` (pronto)

Sem alteração.

---

## 9. Especificação dos testes (TestSet-Func)

### 9.1 Princípios

1. **A especificação da Parte I é o oráculo.** Os valores de entrada, as saídas esperadas e as
   classes exercitadas vêm de `docs/parte-1/especificacao-parte-1.md` (Tabela 2, seção 6).
   Nunca altere a saída esperada de um caso para que ele passe. Se um caso parecer errado,
   pare e reporte; não corrija a especificação por conta própria.
2. Cada caso de teste é **um** método `@Test` com **uma** anotação `@TestCase`.
3. Os campos de `@TestCase` copiam **literalmente** a Tabela 2:
   - `id` = coluna ID (`"CT01"`);
   - `input` = coluna "Condições de Entrada", com os mesmos caracteres (inclusive o `−` de
     `X(−1,0)` e as aspas de `<"a 1">`);
   - `expected` = coluna "Saída Esp.";
   - `classes` = coluna "Classes Eq. Exercitadas", **na mesma ordem**.
4. Nenhum `@Ignore` no TestSet-Func ao final.
5. Nome do método: `ct{NN}_{cenario}_{resultado}` (tabela 9.3).
6. Asserções com `org.junit.Assert` (`assertEquals`, `assertTrue`, `assertNull`,
   `assertThrows`). Uma rejeição é verificada com `assertThrows` e depois com o motivo:

```java
InvalidMoveException error =
        assertThrows(InvalidMoveException.class, () -> play(game, -1, 0));
assertEquals(Reason.POSITION_OUT_OF_BOUNDS, error.getReason());
```

   O `new Position(...)` precisa ficar **dentro** da lambda, porque é ele que lança a exceção
   de posição. Manter o auxiliar privado que já existe em `GameFunctionalTest`:
   `private void play(Game game, int row, int column) { game.play(new Position(row, column)); }`.
7. Não verificar `getCurrentPlayer()` nos casos CT14–CT16: a vez depois do fim da partida não é
   saída especificada na Parte I.

### 9.2 Alinhar `specification/` à Tabela 1

Reescrever `InputCondition` e `EquivalenceClass` para refletir **exatamente** a Tabela 1 da
especificação da Parte I (seção 3.2).

`InputCondition` (nesta ordem; descrição = texto da coluna "Condição de Entrada"):

| Constante | Descrição |
|---|---|
| `INPUT_FORMAT` | `Formato da entrada` |
| `ROW` | `Linha (L)` |
| `COLUMN` | `Coluna (C)` |
| `TARGET_CELL` | `Célula escolhida` |
| `GAME_STATE` | `Estado da partida` |
| `MOVE_RESULT` | `Resultado da jogada válida` |
| `WINNING_PLAYER` | `Jogador que completa a linha` |

`EquivalenceClass` (constante, condição, descrição):

| Classe | Condição | Descrição |
|---|---|---|
| V1 | INPUT_FORMAT | `dois inteiros separados por espaço` |
| I1 | INPUT_FORMAT | `quantidade de valores ≠ 2` |
| I2 | INPUT_FORMAT | `valor não inteiro` |
| V2 | ROW | `0 ≤ L ≤ 2` |
| I3 | ROW | `L < 0` |
| I4 | ROW | `L > 2` |
| V3 | COLUMN | `0 ≤ C ≤ 2` |
| I5 | COLUMN | `C < 0` |
| I6 | COLUMN | `C > 2` |
| V4 | TARGET_CELL | `vazia` |
| I7 | TARGET_CELL | `ocupada` |
| V5 | GAME_STATE | `em andamento` |
| I8 | GAME_STATE | `encerrada – vitória ou empate` |
| V6 | MOVE_RESULT | `completa linha horizontal` |
| V7 | MOVE_RESULT | `completa coluna` |
| V8 | MOVE_RESULT | `completa diagonal principal` |
| V9 | MOVE_RESULT | `completa diagonal secundária` |
| V10 | MOVE_RESULT | `preenche a última célula sem formar linha – empate` |
| V11 | MOVE_RESULT | `não completa linha nem preenche o tabuleiro – partida continua` |
| V12 | WINNING_PLAYER | `X` |
| V13 | WINNING_PLAYER | `O` |

`CauseEffectSpecification` já está igual à seção 5 da Parte I: não alterar.

### 9.3 Casos de teste

Onde cada caso roda segue a seção 7.4 da Parte I: os casos cuja saída inclui mensagem ou que
testam o texto digitado rodam pelo console; os demais, direto no domínio.

**`functional/GameFunctionalTest`** (domínio, chama `game.play(new Position(l, c))`):

| ID | Método | Passos | Asserções |
|---|---|---|---|
| CT03 | `ct03_xCompletesMainDiagonal_xWins` | jogar `Moves.X_WINS_MAIN_DIAGONAL` | status `X_WINS` |
| CT04 | `ct04_xCompletesAntiDiagonal_xWins` | jogar `Moves.X_WINS_ANTI_DIAGONAL` | status `X_WINS` |
| CT06 | `ct06_xCompletesLineOnNinthMove_xWinsNotDraw` | jogar `Moves.X_WINS_ON_NINTH_MOVE` | status `X_WINS`; `board.isFull()` |
| CT07 | `ct07_firstMove_isXAndTurnPassesToO` | jogar (1,1) | status `IN_PROGRESS`; célula (1,1) = `X`; vez de `O` |
| CT08 | `ct08_cornerMoves_allAcceptedAndTurnAlternates` | jogar (0,0), (0,2), (2,0), (2,2) | (0,0)=X, (0,2)=O, (2,0)=X, (2,2)=O; 4 células preenchidas; vez de `X`; `IN_PROGRESS` |
| CT09 | `ct09_rowMinusOne_rejectedOutOfBounds` | jogar (−1,0) | `POSITION_OUT_OF_BOUNDS`; 0 células preenchidas; vez de `X`; `IN_PROGRESS` |
| CT10 | `ct10_rowThree_rejectedOutOfBounds` | jogar (3,0) | idem CT09 |
| CT11 | `ct11_columnMinusOne_rejectedOutOfBounds` | jogar (0,−1) | idem CT09 |
| CT12 | `ct12_columnThree_rejectedOutOfBounds` | jogar (0,3) | idem CT09 |
| CT13 | `ct13_moveOnOccupiedCell_rejectedCellOccupied` | jogar (0,0); jogar (0,0) | 2ª jogada: `CELL_OCCUPIED`; (0,0)=X; 1 célula preenchida; vez de `O` |
| CT14 | `ct14_moveAfterWin_rejectedGameFinished` | jogar `Moves.X_WINS_ROW_0`; jogar (2,2) | `GAME_ALREADY_FINISHED`; status `X_WINS`; (2,2) vazia |
| CT15 | `ct15_occupiedCellAfterDraw_rejectedGameFinished` | jogar `Moves.DRAW`; jogar (0,0) | `GAME_ALREADY_FINISHED` (não `CELL_OCCUPIED`); status `DRAW` |
| CT16 | `ct16_outOfBoundsAfterWin_rejectedOutOfBounds` | jogar `Moves.X_WINS_ROW_0`; jogar (3,3) | `POSITION_OUT_OF_BOUNDS` (não `GAME_ALREADY_FINISHED`); status `X_WINS` |

**`functional/ConsoleFunctionalTest`** (console, `ConsoleGame` com `ScriptedInputReader` e
`RecordingOutputPrinter`; o teste guarda a referência ao `Game` para as asserções):

| ID | Método | Linhas digitadas | Asserções |
|---|---|---|---|
| CT01 | `ct01_xCompletesRow0_xWins` | `Moves.X_WINS_ROW_0` como texto (`"0 0"`, `"1 0"`, ...) | status `X_WINS`; última linha impressa `X venceu!`; nenhuma linha com `Jogada inválida` |
| CT02 | `ct02_oCompletesColumn2_oWins` | `Moves.O_WINS_COLUMN_2` como texto | status `O_WINS`; última linha `O venceu!`; nenhuma `Jogada inválida` |
| CT05 | `ct05_boardFullWithoutLine_draw` | `Moves.DRAW` como texto | status `DRAW`; última linha `Empate!`; nenhuma `Jogada inválida` |
| CT17 | `ct17_nonNumericValue_showsNotANumberMessage` | `"a 1"` | imprimiu `Jogada inválida: 'a' não é um número`; 0 células; vez de `X` |
| CT18 | `ct18_singleValue_showsTwoNumbersMessage` | `"1"` | imprimiu `Jogada inválida: informe exatamente dois números`; 0 células; vez de `X` |
| CT19 | `ct19_threeValues_showsTwoNumbersMessage` | `"1 1 1"` | idem CT18 |
| CT20 | `ct20_emptyLine_showsTwoNumbersMessage` | `""` | idem CT18 |
| CT21 | `ct21_extraSpaces_moveAccepted` | `"  1   1  "` | (1,1)=X; vez de `O`; `IN_PROGRESS`; nenhuma `Jogada inválida` |

Nos casos CT17–CT21 a partida não termina: o roteiro acaba, o `ConsoleGame` recebe
`InputExhaustedException`, imprime `Entrada encerrada. Partida interrompida.` e retorna
(seção 8.3). O teste não deve esperar exceção.

### 9.4 `support/Moves` (novo)

Classe final, não instanciável, com as sequências de jogadas da Tabela 2 como constantes
`int[][]` (cada item é `{linha, coluna}`, na ordem X, O, X, ...), com javadoc indicando o caso
de teste de origem:

| Constante | Jogadas | Usada em |
|---|---|---|
| `X_WINS_ROW_0` | (0,0) (1,0) (0,1) (1,1) (0,2) | CT01, CT14, CT16 |
| `O_WINS_COLUMN_2` | (0,0) (0,2) (1,0) (1,2) (2,1) (2,2) | CT02 |
| `X_WINS_MAIN_DIAGONAL` | (0,0) (0,1) (1,1) (0,2) (2,2) | CT03 |
| `X_WINS_ANTI_DIAGONAL` | (0,2) (0,0) (1,1) (0,1) (2,0) | CT04 |
| `DRAW` | (0,0) (0,1) (0,2) (1,1) (1,0) (2,0) (2,1) (1,2) (2,2) | CT05, CT15 |
| `X_WINS_ON_NINTH_MOVE` | (0,2) (0,1) (1,1) (2,0) (2,1) (1,0) (0,0) (1,2) (2,2) | CT06 |

E dois métodos auxiliares:

- `static void playAll(Game game, int[][] moves)`: aplica as jogadas no domínio;
- `static String[] asTypedLines(int[][] moves)`: converte para as linhas digitadas
  (`{0, 2}` → `"0 2"`).

Todas as sequências foram simuladas: nenhuma termina antes da última jogada.

### 9.5 Suites

- `FunctionalSuite`: `GameFunctionalTest` e `ConsoleFunctionalTest`.
- `FunctionalAndStructuralSuite` e `AllTestsSuite`: sem mudança (já compõem `FunctionalSuite`).

### 9.6 `ReportGenerator`

1. `TestSet` passa a aceitar várias classes: `TestSet(String name, String fileSuffix,
   Class<?>... testClasses)`. Os testes de todas as classes entram na mesma Tabela 2, ordenados
   pelo número do ID. `FUNCTIONAL` = `GameFunctionalTest` + `ConsoleFunctionalTest`.
2. Na Tabela 1, juntar as classes de uma célula como na especificação: separadas por vírgula,
   com `" e "` antes da última (`a (V6), b (V7) e c (V8)`). Com duas: `a (I1) e b (I2)`. Com
   uma: só ela. Implementar como método estático de visibilidade de pacote
   `static String joinWithAnd(List<String> items)` em `ReportGenerator`.
3. Manter tudo o mais como está (checagens, arquivos gerados).
4. Criar `report/ReportGeneratorTest` com testes de `joinWithAnd` para 1, 2 e 3 itens.

---

## 10. Plano de execução

Execute em ordem. Cada fase tem um critério de pronto; não avance com o critério falhando.
Se o projeto estiver em um repositório git, faça um commit ao fim de cada fase
(`fase N: <resumo>`).

### Fase 0 – Preparação

1. Ler esta SPEC e a especificação da Parte I.
2. Rodar `mvn -q test` e confirmar que o build atual passa.
3. Aplicar os ajustes de build da seção 12 (itens 1 a 4).

**Pronto quando:** `mvn verify` passa e gera `target/site/jacoco/index.html`.

### Fase 1 – Especificação em código

1. Reescrever `InputCondition` e `EquivalenceClass` (9.2).
2. Ajustar `ReportGenerator` (9.6), **exceto** a inclusão de `ConsoleFunctionalTest` em
   `FUNCTIONAL`, que só existe na Fase 2. Criar `ReportGeneratorTest`.
3. Atualizar os dois exemplos atuais de `GameFunctionalTest` para usar as novas constantes de
   `EquivalenceClass` (eles serão reescritos na Fase 2; aqui é só para compilar).

**Pronto quando:** `mvn test` passa e a Tabela 1 gerada por
`mvn test-compile exec:java@report` tem o mesmo conteúdo da Tabela 1 da Parte I.

### Fase 2 – TestSet-Func (antes do código)

1. Criar `support/Moves` (9.4).
2. Escrever `GameFunctionalTest` (13 casos) e `ConsoleFunctionalTest` (8 casos) como na 9.3,
   sem `@Ignore`.
3. Atualizar `FunctionalSuite` (9.5) e incluir `ConsoleFunctionalTest` em `FUNCTIONAL` no
   `ReportGenerator` (9.6, item 1).
4. Para os testes compilarem: ajustar `InvalidMoveException` (7.3), adicionar
   `GameStatus.victoryOf` (7.2) e criar o esqueleto de `Board.countFilledCells()` lançando
   `UnsupportedOperationException("TODO")`.

**Pronto quando:** tudo compila (`mvn test-compile`). **Não rode o TestSet-Func ainda**: ele
falharia por falta de implementação, o que não é defeito.

**Daqui até a Fase 5**, para rodar os outros testes sem executar o TestSet-Func, use
`mvn test -Dtest='!*FunctionalTest'` (ou só `mvn test-compile`). `mvn test` e `mvn verify` sem
esse filtro rodariam o TestSet-Func.

### Fase 3 – Domínio

Implementar `Position` (7.4), `Board` (7.5) e `Game` (7.6) por completo, sem rodar o
TestSet-Func. Os demais testes podem rodar com `mvn test -Dtest='!*FunctionalTest'`.

**Pronto quando:** o domínio compila e você considera a implementação completa.

### Fase 4 – Console

Implementar o tratamento de fim de entrada em `ConsoleGame` (8.3) e conferir todas as mensagens
(8.4).

**Pronto quando:** compila e você considera a implementação completa.

### Fase 5 – Primeira execução e defeitos

1. Rodar o TestSet-Func pela primeira vez: `mvn test -Dtest=FunctionalSuite`.
2. Para **cada** caso que falhar, registrar o defeito em `docs/defeitos.md` (seção 11) **antes**
   de corrigir.
3. Corrigir o código (nunca o teste) e rodar de novo, até todos passarem. Registrar a correção e
   o reteste no mesmo item do registro.
4. Se nenhum caso falhar, registrar isso em `docs/defeitos.md`.

**Pronto quando:** os 21 casos passam.

### Fase 6 – Verificação final

Conferir todos os itens da seção 14.

---

## 11. Registro de defeitos (`docs/defeitos.md`)

A Parte II-A exige apresentar no relatório os defeitos encontrados pelo TestSet-Func, a correção
e o reteste. Criar o arquivo com este formato:

```markdown
# Registro de defeitos

| ID | Parte | Caso que revelou | Sintoma (saída obtida) | Causa | Correção | Reteste |
|---|---|---|---|---|---|---|
| DEF-01 | II-A | CT06 | ... | ... | ... | ... |
```

Linha de **exemplo** de preenchimento (não copiar como defeito real):
`DEF-01 | II-A | CT06 | expected:<X_WINS> but was:<DRAW> | empate verificado antes da vitória em
Game.play | ordem das verificações invertida | CT06 passou; TestSet-Func 21/21`.

Os IDs são `DEF-01`, `DEF-02`..., para não confundir com as decisões D1–D10 da Parte I.

Regras:

- Um defeito é uma falha de teste na **primeira execução** do TestSet-Func (Fase 5), ou em
  execuções posteriores causadas pelo código de produção. Erro de compilação não é defeito.
- O sintoma copia a mensagem real de falha do JUnit.
- Não apagar itens; o histórico vai para o relatório.

## 12. Build (`pom.xml`)

1. **Checagem da API do Java 8.** Hoje `source/target 1.8` com JDK 17 deixa compilar uso de API
   do Java 9+ (ex.: `List.of`), que quebra no Baduíno. Adicionar um perfil ativado por
   `<jdk>[9,)</jdk>` que define `<maven.compiler.release>8</maven.compiler.release>`.
2. **Fixar versões dos plugins:** `maven-compiler-plugin` 3.13.0 e `maven-surefire-plugin` 3.2.5.
3. **JaCoCo** (mesmo motor do EclEmma), `jacoco-maven-plugin` 0.8.12: `prepare-agent` na fase
   padrão dele (`initialize`, para o agente estar ativo quando o Surefire rodar) e `report`
   ligado à fase `verify`. Relatório em `target/site/jacoco/index.html`. Serve para
   acompanhar a cobertura por linha de comando; os relatórios entregues continuam sendo os do
   EclEmma e do Baduíno.
4. **PITest por etapa.** Remover `<targetTests>` da configuração (configurado no `pom.xml`, ele
   não pode ser sobrescrito por `-DtargetTests`). A partir daí o PITest **sempre** precisa de
   `-DtargetTests`: sem ele, o padrão do plugin aponta para os pacotes de `targetClasses`, que
   não têm testes. O comando sem `-DtargetTests` deve sair do README. Manter `targetClasses` = `domain.*`,
   `mutators` = `ALL`, HTML. Adicionar `excludedTestClasses` para `report.*`, `causeeffect.*`,
   `io.*` e `suites.*`. Comandos:

```bash
# Parte III-A: TestSet-Func + TestSet-Estr
mvn test-compile org.pitest:pitest-maven:mutationCoverage -DtargetTests="br.ufjf.dcc168.tictactoe.functional.*,br.ufjf.dcc168.tictactoe.structural.*"

# Parte III-B: + testes de mutação
mvn test-compile org.pitest:pitest-maven:mutationCoverage -DtargetTests="br.ufjf.dcc168.tictactoe.functional.*,br.ufjf.dcc168.tictactoe.structural.*,br.ufjf.dcc168.tictactoe.mutation.*"
```

5. Atualizar o `README.md`: comandos acima, `mvn verify` com JaCoCo, remover a nota de que
   `specification/` está desalinhado, e listar `docs/SPEC.md` e `docs/defeitos.md`.

## 13. Fases futuras – NÃO executar sem pedido explícito

Registradas aqui para manter a arquitetura coerente.

### Parte II-B – TestSet-Estr

- Objetivo: 100% de cobertura de fluxo de controle (EclEmma) e de fluxo de dados (Baduíno) no
  domínio e no console, partindo do TestSet-Func.
- Testes em `structural/GameStructuralTest` (remover o `@Ignore` de classe), IDs `CE01`, `CE02`...,
  métodos `ceNN_...`, cada um com comentário do requisito coberto (ramo ou par def-uso).
- `@TestCase` precisará de `classes` opcional (`default {}`) e de um campo `requirement`
  (requisito estrutural); a Tabela 2 do TestSet-Estr mostra o requisito no lugar das classes.
- O JaCoCo ajuda a achar ramos não cobertos; o número final vem do EclEmma/Baduíno no Eclipse.

### Parte III-B – Teste de mutação

- Rodar o PITest da III-A, guardar o escore inicial, e escrever testes em
  `mutation/GameMutationTest`, IDs `CM01`..., cada um com comentário do mutante alvo
  (classe, linha, operador).
- Registrar mutantes equivalentes em `docs/mutantes-equivalentes.md` com a justificativa
  (o relatório pede três explicados).

## 14. Critérios de aceite

- [ ] `mvn verify` passa sem falhas e sem testes ignorados no TestSet-Func.
- [ ] `FunctionalSuite` roda os 21 casos (CT01–CT21) e todos passam.
- [ ] `mvn test-compile exec:java@report` termina **sem avisos** no console (requer o Graphviz
      instalado; se não estiver, o único aviso aceitável é o de Graphviz não encontrado, e isso
      deve constar no resumo final).
- [ ] Tabela 1 gerada = Tabela 1 da Parte I (mesmas linhas e textos).
- [ ] Tabela 2 (TestSet-Func) gerada = Tabela 2 da Parte I nas colunas ID, Condições de
      Entrada, Saída Esp. e Classes; a coluna Saída Obtida repete a saída esperada em todos os
      casos. (O gerador escapa `<` como `\<` no Markdown; isso é esperado.)
- [ ] Tabela de decisão gerada = seção 5.5 da Parte I (R1–R7).
- [ ] Nenhuma importação de `System.in`, `System.out` ou `Scanner` em `domain/`.
- [ ] Nenhum uso de API do Java 9+ (o perfil `release 8` garante na compilação).
- [ ] Código formatado: `mvn fmt:check` passa.
- [ ] `docs/defeitos.md` preenchido (ou declarando que não houve defeitos).
- [ ] Partida jogável, verificada com entrada redirecionada:
      `printf '0 0\n1 0\n0 1\n1 1\n0 2\n' | mvn -q exec:java` termina com `X venceu!`, e a
      sequência `Moves.DRAW` termina com `Empate!`.
- [ ] Resumo final para o grupo com: número de testes, defeitos registrados e a cobertura de
      linha e de ramo do JaCoCo em `domain` e `console` **só com o TestSet-Func**
      (`mvn verify -Dtest=FunctionalSuite`), como referência para a Parte II-A.

## 15. O que fica com o grupo

- Importar no Eclipse e rodar `FunctionalSuite` com EclEmma e Baduíno (Parte II-A); exportar os
  relatórios para `docs/reports/`.
- Instalar o Pitclipse para os screenshots da view PIT Summary (Parte III).
- Gerar o zip de entrega com o projeto Eclipse (`.project`, `.classpath`).
- Escrever o relatório no Google Drive, compartilhado com o professor, com as respostas às
  perguntas das Partes II-C e III-C.
