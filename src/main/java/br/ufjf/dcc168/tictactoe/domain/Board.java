package br.ufjf.dcc168.tictactoe.domain;

/**
 * Tabuleiro 3x3.
 *
 * <p>Responsável apenas por guardar os símbolos e detectar linhas completas. Não conhece turnos nem
 * regras de alternância; isso é papel do Game.
 */
public class Board {

    public static final int SIZE = 3;

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
     * @throws InvalidMoveException com motivo CELL_OCCUPIED se a célula já estiver preenchida
     */
    public void place(Symbol symbol, Position position) {
        // TODO: rejeitar célula ocupada e gravar o símbolo
        throw new UnsupportedOperationException("TODO");
    }

    /** Indica se todas as nove células estão preenchidas. */
    public boolean isFull() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Indica se o símbolo completou alguma linha, coluna ou diagonal. */
    public boolean hasCompleteLine(Symbol symbol) {
        // TODO: verificar as 3 linhas, as 3 colunas e as 2 diagonais
        throw new UnsupportedOperationException("TODO");
    }
}
