package br.ufjf.dcc168.tictactoe.domain;

import br.ufjf.dcc168.tictactoe.domain.InvalidMoveException.Reason;

public class Board {

    public static final int SIZE = 3;

    private static final Position[][] WINNING_LINES = {
        // Linhas
        {new Position(0, 0), new Position(0, 1), new Position(0, 2)},
        {new Position(1, 0), new Position(1, 1), new Position(1, 2)},
        {new Position(2, 0), new Position(2, 1), new Position(2, 2)},
        // Colunas
        {new Position(0, 0), new Position(1, 0), new Position(2, 0)},
        {new Position(0, 1), new Position(1, 1), new Position(2, 1)},
        {new Position(0, 2), new Position(1, 2), new Position(2, 2)},
        // Diagonal principal e diagonal secundária
        {new Position(0, 0), new Position(1, 1), new Position(2, 2)},
        {new Position(0, 2), new Position(1, 1), new Position(2, 0)}
    };

    private final Symbol[][] cells = new Symbol[SIZE][SIZE];

    /** Retorna null se a célula estiver vazia. */
    public Symbol getSymbolAt(Position position) {
        return cells[position.getRow()][position.getColumn()];
    }

    public boolean isEmptyAt(Position position) {
        return getSymbolAt(position) == null;
    }

    public void place(Symbol symbol, Position position) {
        if (!isEmptyAt(position)) {
            throw new InvalidMoveException(Reason.CELL_OCCUPIED);
        }
        cells[position.getRow()][position.getColumn()] = symbol;
    }

    public int countFilledCells() {
        int filledCells = 0;
        for (Symbol[] row : cells) {
            for (Symbol cell : row) {
                if (cell != null) {
                    filledCells++;
                }
            }
        }
        return filledCells;
    }

    public boolean isFull() {
        return countFilledCells() == SIZE * SIZE;
    }

    public boolean hasCompleteLine(Symbol symbol) {
        for (Position[] line : WINNING_LINES) {
            if (isLineFilledWith(line, symbol)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLineFilledWith(Position[] line, Symbol symbol) {
        for (Position position : line) {
            if (getSymbolAt(position) != symbol) {
                return false;
            }
        }
        return true;
    }
}
