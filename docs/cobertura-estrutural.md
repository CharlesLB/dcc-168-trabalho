# Cobertura estrutural (Partes II-A e II-B)

Medições feitas por linha de comando em 07/10/2026, como referência para o relatório. Os números
entregues continuam sendo os do EclEmma e do Baduíno, no Eclipse.

- **Fluxo de controle:** JaCoCo 0.8.12 (mesmo motor do EclEmma), `mvn verify -Dtest=<suite>`.
- **Fluxo de dados:** BA-DUA 0.8.0 (motor do Baduíno, critério todos-os-usos), por
  instrumentação offline com a CLI (veja o fim deste documento).

Escopo: pacotes `domain` e `console`. O pacote `io` fica de fora, porque `ConsoleInputReader` lê
de `System.in` e só roda na partida real.

## Resultados

| Pacote | Suite | Linhas | Ramos | Métodos | Pares def-uso |
|---|---|---|---|---|---|
| `domain` | `FunctionalSuite` (II-A) | 72/73 | 40/40 | 28/29 | 127/131 |
| `console` | `FunctionalSuite` (II-A) | 58/58 | 17/17 | 13/13 | 51/55 |
| `domain` | `FunctionalAndStructuralSuite` (II-B) | 73/73 | 40/40 | 29/29 | 127/131 |
| `console` | `FunctionalAndStructuralSuite` (II-B) | 58/58 | 17/17 | 13/13 | 51/55 |

Os 8 pares def-uso não cobertos são infactíveis (seção abaixo). Descontados eles, a cobertura de
fluxo de dados é de 100% já com o TestSet-Func.

## O que o TestSet-Estr acrescenta

| Caso | Requisito | Por que o TestSet-Func não cobre |
|---|---|---|
| CE01 | `Position.toString` (linha 36) | Nenhum caso da Parte I imprime uma posição. O método não tem ramos nem pares def-uso, então só muda a cobertura de fluxo de controle |

## Pares def-uso infactíveis

Todos têm a mesma causa: o laço percorre um array ou intervalo de tamanho fixo (3 ou 8), então a
variável de controle nunca chega à saída do laço, nem a um teste falso, ainda com o valor inicial.

| Classe.método | Linha | Variável | Definição → uso | Por que é infactível |
|---|---|---|---|---|
| `BoardRenderer.render` | 36 | `row` | `row = 0` → `row < Board.SIZE` (falso, sai do laço) | Com `row = 0`, `0 < 3` é sempre verdadeiro |
| `BoardRenderer.render` | 36 → 38 | `row` | `row = 0` → `row < Board.SIZE - 1` (falso) | Com `row = 0`, `0 < 2` é sempre verdadeiro |
| `BoardRenderer.formatRow` | 46 | `column` | `column = 0` → `column < Board.SIZE` (falso, sai do laço) | Com `column = 0`, `0 < 3` é sempre verdadeiro |
| `BoardRenderer.formatRow` | 46 → 48 | `column` | `column = 0` → `column < Board.SIZE - 1` (falso) | Com `column = 0`, `0 < 2` é sempre verdadeiro |
| `Board.countFilledCells` | 44 | índice do `for` sobre `cells` | índice `= 0` → saída do laço | `cells` sempre tem 3 linhas |
| `Board.countFilledCells` | 45 | índice do `for` sobre `row` | índice `= 0` → saída do laço | Cada linha sempre tem 3 células |
| `Board.hasCompleteLine` | 59 | índice do `for` sobre `WINNING_LINES` | índice `= 0` → saída do laço | `WINNING_LINES` sempre tem 8 linhas |
| `Board.isLineFilledWith` | 68 | índice do `for` sobre `line` | índice `= 0` → saída do laço | Toda linha vencedora tem 3 posições |

Nos quatro laços `for (X x : array)` de `Board`, o índice é uma variável que o compilador cria e
não aparece no código-fonte. O BA-DUA conta os pares dela, mas o relatório XML não os nomeia. A
identificação acima vem da contagem por método (1 par por laço) e da mesma análise dos laços com
índice explícito de `BoardRenderer`, que o relatório nomeia.

## Observações para a medição no Eclipse

- A versão 0.6.0 do BA-DUA falha ao instrumentar `ConsoleGame` ("Error while instrumenting
  class"); a 0.8.0 funciona. Se o Baduíno não mostrar `ConsoleGame`, confira a versão do BA-DUA
  embutida no plugin.
- O JaCoCo (e o EclEmma) ignora o construtor privado vazio de `MoveParser`, os métodos `values` e
  `valueOf` dos enums e a classe sintética do `switch` em `ConsoleGame`. Versões antigas do
  EclEmma podem contá-los.

## Como reproduzir o BA-DUA

Com o `ba-dua-0.8.0.zip` (Maven Central, `br.usp.each.saeg:ba-dua`) descompactado em `$BADUA`,
depois de `mvn test-compile` e `mvn dependency:build-classpath -Dmdep.outputFile=cp.txt`:

```bash
java -jar $BADUA/lib/ba-dua-cli-0.8.0-all.jar instrument -src target/classes -dest instrumented
java -Doutput.file=coverage.ser \
     -cp instrumented:target/test-classes:$BADUA/lib/ba-dua-agent-rt-0.8.0-all.jar:$(cat cp.txt) \
     org.junit.runner.JUnitCore br.ufjf.dcc168.tictactoe.suites.FunctionalAndStructuralSuite
java -jar $BADUA/lib/ba-dua-cli-0.8.0-all.jar report -input coverage.ser -classes target/classes \
     -show-classes -show-methods -xml coverage.xml
```

O modo agente (`-javaagent`) derruba o JDK 21; a instrumentação offline evita o problema.
