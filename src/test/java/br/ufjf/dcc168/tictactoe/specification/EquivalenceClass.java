package br.ufjf.dcc168.tictactoe.specification;

import static br.ufjf.dcc168.tictactoe.specification.InputCondition.COLUMN;
import static br.ufjf.dcc168.tictactoe.specification.InputCondition.GAME_STATE;
import static br.ufjf.dcc168.tictactoe.specification.InputCondition.INPUT_FORMAT;
import static br.ufjf.dcc168.tictactoe.specification.InputCondition.MOVE_RESULT;
import static br.ufjf.dcc168.tictactoe.specification.InputCondition.ROW;
import static br.ufjf.dcc168.tictactoe.specification.InputCondition.TARGET_CELL;
import static br.ufjf.dcc168.tictactoe.specification.InputCondition.WINNING_PLAYER;

public enum EquivalenceClass {
    V1(INPUT_FORMAT, "dois inteiros separados por espaço"),
    I1(INPUT_FORMAT, "quantidade de valores ≠ 2"),
    I2(INPUT_FORMAT, "valor não inteiro"),

    V2(ROW, "0 ≤ L ≤ 2"),
    I3(ROW, "L < 0"),
    I4(ROW, "L > 2"),

    V3(COLUMN, "0 ≤ C ≤ 2"),
    I5(COLUMN, "C < 0"),
    I6(COLUMN, "C > 2"),

    V4(TARGET_CELL, "vazia"),
    I7(TARGET_CELL, "ocupada"),

    V5(GAME_STATE, "em andamento"),
    I8(GAME_STATE, "encerrada – vitória ou empate"),

    V6(MOVE_RESULT, "completa linha horizontal"),
    V7(MOVE_RESULT, "completa coluna"),
    V8(MOVE_RESULT, "completa diagonal principal"),
    V9(MOVE_RESULT, "completa diagonal secundária"),
    V10(MOVE_RESULT, "preenche a última célula sem formar linha – empate"),
    V11(MOVE_RESULT, "não completa linha nem preenche o tabuleiro – partida continua"),

    V12(WINNING_PLAYER, "X"),
    V13(WINNING_PLAYER, "O");

    private final InputCondition condition;
    private final String description;

    EquivalenceClass(InputCondition condition, String description) {
        this.condition = condition;
        this.description = description;
    }

    public InputCondition getCondition() {
        return condition;
    }

    public boolean isValid() {
        return name().startsWith("V");
    }

    public String toTableText() {
        return description + " (" + name() + ")";
    }
}
