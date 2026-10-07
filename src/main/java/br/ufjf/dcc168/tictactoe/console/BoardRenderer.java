package br.ufjf.dcc168.tictactoe.console;

import br.ufjf.dcc168.tictactoe.domain.Board;
import br.ufjf.dcc168.tictactoe.domain.Position;
import br.ufjf.dcc168.tictactoe.domain.Symbol;
import br.ufjf.dcc168.tictactoe.io.OutputPrinter;

/**
 * Desenha o tabuleiro no console.
 *
 * <p>Exemplo de saída:
 *
 * <pre>
 *     0   1   2
 * 0   X |   | O
 *    ---+---+---
 * 1     | X |
 *    ---+---+---
 * 2     |   | O
 * </pre>
 */
public class BoardRenderer {

    private static final String EMPTY_CELL = " ";
    private static final String COLUMN_HEADER = "    0   1   2";
    private static final String ROW_SEPARATOR = "   ---+---+---";

    private final OutputPrinter printer;

    public BoardRenderer(OutputPrinter printer) {
        this.printer = printer;
    }

    public void render(Board board) {
        printer.printLine(COLUMN_HEADER);
        for (int row = 0; row < Board.SIZE; row++) {
            printer.printLine(formatRow(board, row));
            if (row < Board.SIZE - 1) {
                printer.printLine(ROW_SEPARATOR);
            }
        }
    }

    private String formatRow(Board board, int row) {
        StringBuilder line = new StringBuilder(row + "  ");
        for (int column = 0; column < Board.SIZE; column++) {
            line.append(' ').append(formatCell(board, new Position(row, column))).append(' ');
            if (column < Board.SIZE - 1) {
                line.append('|');
            }
        }
        return line.toString().replaceAll("\\s+$", "");
    }

    private String formatCell(Board board, Position position) {
        Symbol symbol = board.getSymbolAt(position);
        return symbol == null ? EMPTY_CELL : symbol.name();
    }
}
