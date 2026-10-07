package br.ufjf.dcc168.tictactoe.domain;

/**
 * Coordenada de uma célula do tabuleiro (objeto de valor imutável).
 *
 * <p>Convenção: linhas e colunas numeradas de 0 a 2. Se o grupo decidir usar 1 a 3, a conversão
 * deve acontecer na camada de console, nunca aqui.
 */
public final class Position {

    public static final int MIN_INDEX = 0;
    public static final int MAX_INDEX = 2;

    private final int row;
    private final int column;

    /**
     * @throws InvalidMoveException com motivo POSITION_OUT_OF_BOUNDS se a linha ou a coluna estiver
     *     fora do intervalo [0, 2]
     */
    public Position(int row, int column) {
        // TODO: validar os limites de row e column
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    // TODO: implementar equals e hashCode (posições com mesma linha e coluna são iguais)

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}
