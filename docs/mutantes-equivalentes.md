# Teste de mutação (Partes III-A e III-B)

PITest 1.15.8, todos os operadores (`ALL`), aplicado ao pacote `domain`. Medição feita por linha
de comando em 07/10/2026 (comandos no README). Os screenshots entregues continuam sendo os do
Pitclipse.

## Escores

| Etapa | Conjuntos de teste | Mutantes | Mortos | Vivos | Escore |
|---|---|---|---|---|---|
| III-A (inicial) | TestSet-Func + TestSet-Estr | 134 | 130 | 4 | 97% |
| III-B (final) | + casos de mutação (CM01) | 134 | 132 | 2 | 99% |

Os 2 mutantes que continuam vivos são equivalentes. Descontados eles, o escore final é 132/132
(100%).

## Mutantes vivos na III-A

| Classe | Linha | Operador (PITest) | Mutação | Situação |
|---|---|---|---|---|
| `InvalidMoveException$Reason` | 17 | `EMPTY_RETURNS` | `getMessage` retorna `""` | Morto pelo CM01 |
| `InvalidMoveException` | 24 | `NON_VOID_METHOD_CALLS` | `super(reason.getMessage())` vira `super(null)` | Morto pelo CM01 |
| `Board` | 23 | `INLINE_CONSTS` | 1ª dimensão de `new Symbol[SIZE][SIZE]`: 3 vira 4 | Equivalente (M1) |
| `Board` | 23 | `INLINE_CONSTS` | 2ª dimensão de `new Symbol[SIZE][SIZE]`: 3 vira 4 | Equivalente (M2) |

Os dois primeiros sobreviveram porque nenhum caso anterior confere o texto de uma
`InvalidMoveException`. O TestSet-Func verifica só o `Reason`, e os casos de console da Parte I
conferem apenas as mensagens de `InvalidInputException`. O CM01 digita `3 0` no console e
confere a mensagem exata da SPEC (seção 8.4): `Jogada inválida: posição fora do tabuleiro (use
valores de 0 a 2)`.

## Mutantes equivalentes

### M1 – `Board`, linha 23: tabuleiro com 4 linhas

```java
private final Symbol[][] cells = new Symbol[SIZE][SIZE];   // original: 3 x 3
private final Symbol[][] cells = new Symbol[4][SIZE];      // mutante:  4 x 3
```

A linha extra (`cells[3]`) nunca recebe símbolo e nunca é lida de forma que mude um resultado:

- `getSymbolAt` e `place` usam `position.getRow()`, que `Position` garante estar em `[0, 2]`.
  A linha 3 é inacessível.
- `countFilledCells` percorre todas as linhas, inclusive a extra, mas só conta células diferentes
  de `null`. A linha extra tem três `null` e soma 0.
- `isFull` compara essa contagem com `SIZE * SIZE` (9), que não depende do array.
- `hasCompleteLine` só consulta as posições de `WINNING_LINES`, todas dentro de `[0, 2]`.
- O array não sai da classe: nenhum método devolve `cells` ou o tamanho dele, e o
  `BoardRenderer` desenha usando `Board.SIZE` e `getSymbolAt`.

Para toda sequência de jogadas, o original e o mutante devolvem os mesmos valores e lançam as
mesmas exceções. Nenhum teste distingue os dois.

### M2 – `Board`, linha 23: linhas com 4 células

```java
private final Symbol[][] cells = new Symbol[SIZE][4];      // mutante: 3 x 4
```

A justificativa é a mesma, agora para a coluna extra: `position.getColumn()` também está em
`[0, 2]`, a coluna 3 fica sempre `null`, `countFilledCells` não a conta e nenhum outro método a
consulta.

Nos dois casos, o mutante só muda espaço de memória que o programa nunca observa. O único jeito
de matá-los seria inspecionar o campo privado por reflexão, e isso testaria a implementação, não
o comportamento especificado.

### Sobre o terceiro mutante

O enunciado pede três mutantes equivalentes explicados, mas com o código atual o PITest só gera
esses dois. Todos os outros 132 foram mortos, e um mutante morto não é equivalente. O número
depende do código: os pares infactíveis da Parte II-B (`docs/cobertura-estrutural.md`) estão em
`BoardRenderer`, fora do alvo do PITest, que muta só o `domain`.
