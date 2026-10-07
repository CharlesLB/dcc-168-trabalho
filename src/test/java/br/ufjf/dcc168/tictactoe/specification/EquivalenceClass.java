package br.ufjf.dcc168.tictactoe.specification;

import static br.ufjf.dcc168.tictactoe.specification.InputCondition.COLUMN;
import static br.ufjf.dcc168.tictactoe.specification.InputCondition.GAME_STATE;
import static br.ufjf.dcc168.tictactoe.specification.InputCondition.ROW;
import static br.ufjf.dcc168.tictactoe.specification.InputCondition.TARGET_CELL;

/**
 * Catálogo das classes de equivalência: fonte única da Tabela 1.
 *
 * <p>Convenção: o nome começa com V (válida) ou I (inválida), igual à notação da Tabela 1. Como os
 * testes referenciam estas constantes em {@link
 * br.ufjf.dcc168.tictactoe.report.TestCase#classes()}, um erro de digitação vira erro de compilação
 * em vez de um erro no relatório.
 *
 * <p>EXEMPLOS: o grupo deve revisar e completar esta lista na Parte I.
 */
public enum EquivalenceClass {
    V1(ROW, "0 ≤ linha ≤ 2"),
    I1(ROW, "linha < 0"),
    I2(ROW, "linha > 2"),

    V2(COLUMN, "0 ≤ coluna ≤ 2"),
    I3(COLUMN, "coluna < 0"),
    I4(COLUMN, "coluna > 2"),

    V3(TARGET_CELL, "célula vazia"),
    I5(TARGET_CELL, "célula ocupada"),

    V4(GAME_STATE, "partida em andamento"),
    I6(GAME_STATE, "partida encerrada (vitória ou empate)");

    private final InputCondition condition;
    private final String description;

    EquivalenceClass(InputCondition condition, String description) {
        this.condition = condition;
        this.description = description;
    }

    public InputCondition getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public boolean isValid() {
        return name().startsWith("V");
    }

    /** Texto usado na Tabela 1, ex.: "linha < 0 (I1)". */
    public String toTableText() {
        return description + " (" + name() + ")";
    }
}
