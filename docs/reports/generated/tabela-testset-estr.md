**Casos de Teste (TestSet-Estr)**

| ID | Condições de Entrada | Saída Esp. | Requisito Estrutural Coberto | Saída Obtida |
|---|---|---|---|---|
| CE01 | Position(1, 2).toString() | "(1, 2)" | Position.toString (linha 33): nós não executados pelo TestSet-Func | "(1, 2)" |
| CE02 | ConsoleInputReader sobre o texto "0 0\n1 2\n"; readLine() duas vezes | "0 0" e depois "1 2" | ConsoleInputReader.readLine: ramo falso de !hasNextLine() (linha 22 para a 25) e pares def-uso de this.scanner: definição na linha 22, usos em (22, 25) e na linha 25 | "0 0" e depois "1 2" |
| CE03 | ConsoleInputReader sobre o texto vazio; readLine() | InputExhaustedException | ConsoleInputReader.readLine: ramo verdadeiro de !hasNextLine() (linha 22 para a 23) e par def-uso de this.scanner: definição na linha 22, uso em (22, 23) | InputExhaustedException |
| CE04 | ConsoleOutputPrinter sobre um fluxo em memória; printLine("Vez de X") | "Vez de X" seguido de quebra de linha | ConsoleOutputPrinter(PrintStream) e printLine (linhas 14 e 19): nós não executados pelo TestSet-Func | "Vez de X" seguido de quebra de linha |
| CE05 | Main.main com a entrada padrão "0 0\n1 0\n0 1\n1 1\n0 2\n" | Última linha da saída padrão: "X venceu!" | Main.main (linhas 13 a 15) e os construtores sem argumento de ConsoleInputReader e ConsoleOutputPrinter (linhas 13 e 10): nós não executados pelo TestSet-Func | Última linha da saída padrão: "X venceu!" |
