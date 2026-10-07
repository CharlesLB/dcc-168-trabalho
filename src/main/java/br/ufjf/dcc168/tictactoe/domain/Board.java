package br.ufjf.dcc168.tictactoe.domain;

import br.ufjf.dcc168.tictactoe.domain.InvalidMoveException.Reason;

/**
 * Tabuleiro 3x3.
 *
 * <p>Responsável apenas por guardar os símbolos e detectar linhas completas. Não conhece turnos nem
 * regras de alternância; isso é papel do Game.
 */
public class Board {

    public static final int SIZE = 3;

    /** As 8 linhas vencedoras: 3 linhas, 3 colunas e 2 diagonais. */
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

    /** Retorna o símbolo da célula, ou null se estiver vazia. */
    public Symbol getSymbolAt(Position position) {
        return cells[position.getRow()][position.getColumn()];
    }

    public boolean isEmptyAt(Position position) {
        return getSymbolAt(position) == null;
    }

    /**
     * Coloca o símbolo na célula.
     *
     * @throws InvalidMoveException com motivo CELL_OCCUPIED se a célula já estiver preenchida;
     *     nesse caso o tabuleiro não muda
     */
    public void place(Symbol symbol, Position position) {
        if (!isEmptyAt(position)) {
            throw new InvalidMoveException(Reason.CELL_OCCUPIED);
        }
        cells[position.getRow()][position.getColumn()] = symbol;
    }

    /** Quantidade de células preenchidas (0 a 9). */
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

    /** Indica se todas as nove células estão preenchidas. */
    public boolean isFull() {
        return countFilledCells() == SIZE * SIZE;
    }

    /** Indica se o símbolo completou alguma linha, coluna ou diagonal. */
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
