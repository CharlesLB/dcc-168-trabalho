**Tabela 1 – Classes de Equivalência**

| Condição de Entrada | Classes de Equivalência Válidas | Classes de Equivalência Inválidas |
|---|---|---|
| Formato da entrada | dois inteiros separados por espaço (V1) | quantidade de valores ≠ 2 (I1) e valor não inteiro (I2) |
| Linha (L) | 0 ≤ L ≤ 2 (V2) | L \< 0 (I3) e L > 2 (I4) |
| Coluna (C) | 0 ≤ C ≤ 2 (V3) | C \< 0 (I5) e C > 2 (I6) |
| Célula escolhida | vazia (V4) | ocupada (I7) |
| Estado da partida | em andamento (V5) | encerrada – vitória ou empate (I8) |
| Resultado da jogada válida | completa linha horizontal (V6), completa coluna (V7), completa diagonal principal (V8), completa diagonal secundária (V9), preenche a última célula sem formar linha – empate (V10) e não completa linha nem preenche o tabuleiro – partida continua (V11) | – |
| Jogador que completa a linha | X (V12) e O (V13) | – |
