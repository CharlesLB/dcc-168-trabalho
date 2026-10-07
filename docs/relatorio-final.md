# Relatório Final – Jogo da Velha (DCC168 Teste de Software, 2026-3)

> **Rascunho para o Google Drive.** Os números vêm da medição por linha de comando (JaCoCo, BA-DUA
> e PITest; arquivos em `docs/reports/linha-de-comando/`). Antes de entregar:
>
> - troque os números pelos do EclEmma, do Baduíno e do Pitclipse e insira os screenshots onde
>   está marcado **[INSERIR]**;
> - escreva as partes marcadas **[GRUPO]**: são as análises pessoais e as perguntas
>   direcionadas ao grupo. Os tópicos abaixo delas trazem só fatos do projeto, para apoiar a
>   resposta;
> - exporte o documento final como `docs/relatorio-final.pdf` para o zip.

**Integrantes:** [GRUPO: nomes]

**Repositório / projeto Eclipse:** `tic-tac-toe` (Java 8, JUnit 4.13.2)

---

## 1. Visão geral

O programa é o Jogo da Velha para console. O código foi separado em camadas para que as regras
pudessem ser testadas sem teclado nem tela:

| Pacote | Conteúdo | Alvo de qual medição |
|---|---|---|
| `domain` | Regras: `Game`, `Board`, `Position`, `GameStatus`, `Symbol`, `InvalidMoveException` | EclEmma, Baduíno e PITest |
| `console` | Interação: `ConsoleGame`, `MoveParser`, `BoardRenderer` | EclEmma e Baduíno |
| `io` e `Main` | Leitura de `System.in` e escrita em `System.out` | Fora do escopo (veja a seção 2.3) |

Conjuntos de teste e suites JUnit usadas em cada etapa:

| Conjunto | Casos | Classe de teste | Suite da etapa |
|---|---|---|---|
| TestSet-Func | 21 (CT01–CT21) | `functional/GameFunctionalTest`, `functional/ConsoleFunctionalTest` | `FunctionalSuite` (II-A) |
| TestSet-Estr | 1 (CE01) | `structural/GameStructuralTest` | `FunctionalAndStructuralSuite` (II-B, III-A) |
| Casos de mutação | 1 (CM01) | `mutation/GameMutationTest` | `AllTestsSuite` (III-B) |

Cada caso de teste é um método `@Test` com uma anotação `@TestCase`, que copia o ID, a entrada,
a saída esperada e as classes (ou o requisito) da especificação. As tabelas do relatório saem
dessas anotações (`ReportGenerator`), então a tabela e o código não divergem.

---

## 2. Parte II – Teste Estrutural

### 2.1 Parte II-A – Automatização do TestSet-Func

Os 21 casos da Parte I foram escritos em JUnit **antes** da implementação do domínio. Os casos
das regras (CT03, CT04, CT06–CT16) chamam o domínio direto; os que dependem de mensagens ou do
texto digitado (CT01, CT02, CT05, CT17–CT21) rodam o console com entrada simulada.

**Primeira execução:** 21 casos executados, 21 passaram. **Nenhum defeito encontrado**
(`docs/defeitos.md`), então não houve correção nem reteste.

**Cobertura do TestSet-Func** [INSERIR: relatórios do EclEmma e do Baduíno]

| Pacote | Instruções | Ramos | Linhas | Métodos | Pares def-uso (todos-os-usos) |
|---|---|---|---|---|---|
| `domain` | 586/603 (97,2%) | 40/40 (100%) | 72/73 (98,6%) | 28/29 | 127/131 (96,9%) |
| `console` | 251/251 (100%) | 17/17 (100%) | 58/58 (100%) | 13/13 | 51/55 (92,7%) |

O único trecho não executado é `Position.toString`, que nenhum caso da Parte I usa.

### 2.2 Parte II-B – TestSet-Estr

**Requisitos não cobertos pelo TestSet-Func e casos criados:**

| Requisito | Critério | Caso | Resultado |
|---|---|---|---|
| Nós da linha 42 (`Position.toString`) | Fluxo de controle | CE01: `Position(1, 2).toString()` → `"(1, 2)"` | Coberto |
| 8 pares def-uso de variáveis de laço | Fluxo de dados | – | Infactíveis (abaixo) |

**Pares def-uso infactíveis.** Os 8 pares que faltam têm a mesma forma: a variável de controle do
laço sai do laço, ou cai num teste falso, ainda com o valor inicial 0. Como os laços percorrem
tamanhos fixos (3 linhas, 3 colunas, 8 linhas vencedoras), nenhuma entrada percorre esse caminho.

| Classe.método | Linha | Variável | Par |
|---|---|---|---|
| `BoardRenderer.render` | 36 | `row` | `row = 0` → `row < Board.SIZE` falso |
| `BoardRenderer.render` | 36 → 38 | `row` | `row = 0` → `row < Board.SIZE - 1` falso |
| `BoardRenderer.formatRow` | 46 | `column` | `column = 0` → `column < Board.SIZE` falso |
| `BoardRenderer.formatRow` | 46 → 48 | `column` | `column = 0` → `column < Board.SIZE - 1` falso |
| `Board.countFilledCells` | 57 | índice do laço externo | `= 0` → saída do laço |
| `Board.countFilledCells` | 58 | índice do laço interno | `= 0` → saída do laço |
| `Board.hasCompleteLine` | 74 | índice do laço | `= 0` → saída do laço |
| `Board.isLineFilledWith` | 83 | índice do laço | `= 0` → saída do laço |

Detalhes e justificativa completa: `docs/cobertura-estrutural.md`.

**Cobertura final (TestSet-Func + TestSet-Estr)** [INSERIR: relatórios do EclEmma e do Baduíno]

| Pacote | Instruções | Ramos | Linhas | Métodos | Pares def-uso |
|---|---|---|---|---|---|
| `domain` | 603/603 (100%) | 40/40 (100%) | 73/73 (100%) | 29/29 | 127/131 (100% dos factíveis) |
| `console` | 251/251 (100%) | 17/17 (100%) | 58/58 (100%) | 13/13 | 51/55 (100% dos factíveis) |

**Defeitos nesta subfase:** nenhum.

### 2.3 Escopo da medição

`Main` e o pacote `io` (`ConsoleInputReader`, `ConsoleOutputPrinter`) só repassam dados de e
para `System.in` e `System.out`. Os testes usam dublês dessas interfaces (`ScriptedInputReader`,
`RecordingOutputPrinter`), então essas classes ficam fora dos conjuntos de teste e da meta de
100%. No EclEmma, o total do projeto aparece abaixo de 100% por causa delas.
[GRUPO: confirmar se o professor aceita esse escopo ou se prefere cobri-las também.]

### 2.4 Parte II-C – Análise

**Eficiência e eficácia das técnicas de Teste Funcional e Estrutural** [GRUPO]

Fatos do projeto que podem apoiar a análise:

- O TestSet-Func (21 casos) cobriu sozinho 100% dos ramos e 100% dos pares def-uso factíveis.
  O teste estrutural acrescentou um caso, para um método que a especificação não usa.
- Nenhuma das duas técnicas revelou defeito. Os testes foram escritos antes do código, a partir
  de um oráculo detalhado (decisões D1–D10 da Parte I).
- O trabalho do teste estrutural foi, quase todo, provar que 8 pares def-uso são infactíveis, e
  não escrever casos novos.
- A cobertura estrutural mostra o que foi executado, não o que foi verificado. A Parte III mostra
  um exemplo: as mensagens de erro do domínio eram executadas (100% de cobertura), mas nenhum
  caso conferia o texto delas.

**Pergunta ao grupo: escrever os casos funcionais primeiro (TDD) influenciou o código? Como?**
[GRUPO]

Fatos do projeto que podem apoiar a resposta:

- O TestSet-Func foi escrito e comitado antes do domínio existir (os casos falhavam com
  `UnsupportedOperationException: TODO` até a implementação).
- `InvalidMoveException.Reason` tem um motivo para cada classe de equivalência inválida de
  jogada (`POSITION_OUT_OF_BOUNDS`, `CELL_OCCUPIED`, `GAME_ALREADY_FINISHED`), e os testes
  conferem o motivo com `assertEquals`.
- A ordem das verificações da decisão D8 virou estrutura: `Position` valida os limites no
  construtor, então uma posição fora do tabuleiro é rejeitada antes de chegar ao `Game`, mesmo
  com a partida encerrada (CT16).
- A vitória é verificada antes do empate em `Game.play` porque o CT06 exige (D9).
- O console lê e escreve por interfaces (`InputReader`, `OutputPrinter`) para que os casos de
  console rodassem sem teclado. Os casos cujo roteiro acaba antes do fim da partida (CT17–CT21)
  levaram ao tratamento de fim da entrada no `ConsoleGame`.
- O código evita validações além da especificação: cada ramo extra exigiria um caso estrutural e
  geraria mutantes.

---

## 3. Parte III – Teste de Mutação

PITest 1.15.8, todos os operadores (`ALL`, equivalente ao Full Mutation), aplicado ao pacote
`domain`. [INSERIR: screenshots da view PIT Summary]

### 3.1 Parte III-A – Qualidade do TestSet-Func + TestSet-Estr

| Mutantes gerados | Mortos | Vivos | Escore de mutação |
|---|---|---|---|
| 134 | 130 | 4 | 97% |

Mutantes vivos:

| Classe | Linha | Operador | Mutação |
|---|---|---|---|
| `InvalidMoveException$Reason` | 24 | `EMPTY_RETURNS` | `getMessage` retorna `""` |
| `InvalidMoveException` | 31 | `NON_VOID_METHOD_CALLS` | `super(reason.getMessage())` vira `super(null)` |
| `Board` | 30 | `INLINE_CONSTS` | 1ª dimensão de `new Symbol[SIZE][SIZE]`: 3 vira 4 |
| `Board` | 30 | `INLINE_CONSTS` | 2ª dimensão de `new Symbol[SIZE][SIZE]`: 3 vira 4 |

**Defeitos nesta subfase:** nenhum.

### 3.2 Parte III-B – Novos casos e mutantes equivalentes

Os dois primeiros mutantes sobreviveram porque nenhum caso conferia o **texto** de uma
`InvalidMoveException`: os casos de domínio conferem o motivo (`Reason`), e os de console só
conferem as mensagens de entrada mal formatada. O caso novo:

| ID | Entrada | Saída esperada | Mutantes alvo |
|---|---|---|---|
| CM01 | `<"3 0">` pelo console | Mensagem `Jogada inválida: posição fora do tabuleiro (use valores de 0 a 2)` | `InvalidMoveException` linhas 24 e 31 |

**Resultado final**

| Mutantes gerados | Mortos | Vivos (equivalentes) | Escore | Escore descontando equivalentes |
|---|---|---|---|---|
| 134 | 132 | 2 | 99% | 100% |

- **Quantidade final de casos de teste:** 23 (21 do TestSet-Func, 1 do TestSet-Estr, 1 de
  mutação).
- **Total de mutantes equivalentes:** 2.
- **Defeitos nesta subfase:** nenhum.

**Mutantes equivalentes explicados**

1. **`Board`, linha 30, `INLINE_CONSTS`: `new Symbol[4][SIZE]`.** O tabuleiro ganha uma 4ª
   linha que nunca é usada. `getSymbolAt` e `place` só recebem posições com linha em `[0, 2]`
   (garantido por `Position`). `countFilledCells` percorre a linha extra, mas ela só tem `null`
   e soma 0. `isFull` compara com a constante 9, e `hasCompleteLine` só consulta posições das 8
   linhas vencedoras. Nenhum método expõe o array. Para qualquer sequência de jogadas, as saídas
   e exceções são as mesmas do original.
2. **`Board`, linha 30, `INLINE_CONSTS`: `new Symbol[SIZE][4]`.** O mesmo raciocínio para uma 4ª
   coluna: `getColumn()` também está em `[0, 2]`, e a coluna extra fica sempre `null`.
3. [GRUPO: o enunciado pede três, mas o PITest gerou só dois equivalentes no `domain`. Veja
   `docs/mutantes-equivalentes.md`.]

### 3.3 Parte III-C – Análise

**Eficiência e eficácia do Teste de Mutação** [GRUPO]

Fatos do projeto que podem apoiar a análise:

- Com 100% de cobertura de instruções, ramos e pares factíveis, o PITest ainda achou 2 mutantes
  matáveis. Eles mostraram uma fraqueza do oráculo (mensagens executadas mas não conferidas), não
  um defeito do programa.
- Um único caso novo (CM01) matou os dois.
- Analisar os equivalentes exigiu raciocinar sobre todo o uso do array `cells`, e não só sobre a
  linha mutada.
- Custo: 134 mutantes e 310 execuções de teste, em poucos segundos para um programa deste porte.

**Pergunta ao grupo: se os mutantes com comportamento diferente do original são mortos, como
saber que o mutante morto não é a versão correta do programa? Como resolver isso?** [GRUPO]

Tópicos para a resposta:

- Um mutante morre quando algum caso de teste dá resultado diferente do **esperado**. O teste
  de mutação supõe que o original está correto (hipótese do programador competente) e usa a
  saída do original como referência.
- Essa suposição só é segura se a saída esperada vier da **especificação**, e não da execução do
  original. Neste trabalho, as saídas esperadas vêm do oráculo da Parte I (decisões D1–D10), não
  do código.
- Quando um mutante morre, o caso que o matou deve ser conferido contra a especificação. Se a
  especificação concordar com o mutante, quem tem defeito é o original. Esse defeito vai para o
  relatório, o código é corrigido e o programa é retestado.
- Se a especificação não decidir, a dúvida é da especificação. A saída é tomar uma decisão
  explícita, como as D1–D10, e registrá-la como parte do oráculo.

---

## 4. Artefatos entregues

| Item do enunciado | Onde está |
|---|---|
| i) Projeto Java/Eclipse e casos JUnit | `pom.xml`, `src/`, `.project`, `.classpath`, `.settings/` |
| ii) Relatórios do EclEmma e do Baduíno | `docs/reports/` [INSERIR] |
| iii) Relatório do PITest (screenshots do PIT Summary) | `docs/reports/` [INSERIR] |
| iv) Relatório final | Google Drive (link na entrega) e `docs/relatorio-final.pdf` |
| Apoio | `docs/defeitos.md`, `docs/cobertura-estrutural.md`, `docs/mutantes-equivalentes.md`, `docs/reports/linha-de-comando/`, `docs/reports/generated/` (tabelas) |

O zip é montado por `scripts/empacotar-entrega.sh <nome>`.
