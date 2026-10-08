**Tabela de Decisão – Grafo de Causa-Efeito**

| Causa / Efeito | R1 | R2 | R3 | R4 | R5 | R6 | R7 |
|---|---|---|---|---|---|---|---|
| C1 – Linha entre 0 e 2 | – | F | V | V | V | V | V |
| C2 – Coluna entre 0 e 2 | F | V | V | V | V | V | V |
| C3 – Partida em andamento | – | – | F | V | V | V | V |
| C4 – Célula escolhida vazia | – | – | – | F | V | V | V |
| C5 – Jogada completa linha, coluna ou diagonal | – | – | – | – | V | F | F |
| C6 – Jogada preenche a última célula vazia | – | – | – | – | – | V | F |
| E1 – Rejeita: posição fora do tabuleiro | X | X |  |  |  |  |  |
| E2 – Rejeita: partida já encerrada |  |  | X |  |  |  |  |
| E3 – Rejeita: célula ocupada |  |  |  | X |  |  |  |
| E4 – Jogador da vez vence |  |  |  |  | X |  |  |
| E5 – Partida termina empatada |  |  |  |  |  | X |  |
| E6 – Vez passa ao adversário |  |  |  |  |  |  | X |
