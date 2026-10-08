package br.ufjf.dcc168.tictactoe.domain;

import br.ufjf.dcc168.tictactoe.domain.InvalidMoveException.Reason;

/**
 * Como a validação acontece na construção, uma posição fora do tabuleiro é rejeitada antes de
 * chegar ao Game, mesmo com a partida encerrada (D8).
 */
public final class Position {

    public static final int MIN_INDEX = 0;
    public static final int MAX_INDEX = 2;

    private final int row;
    private final int column;

    public Position(int row, int column) {
        if (isOutOfBounds(row) || isOutOfBounds(column)) {
            throw new InvalidMoveException(Reason.POSITION_OUT_OF_BOUNDS);
        }
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    private static boolean isOutOfBounds(int index) {
        return index < MIN_INDEX || index > MAX_INDEX;
    }
}
