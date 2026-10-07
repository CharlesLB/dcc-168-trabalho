package br.ufjf.dcc168.tictactoe.support;

import br.ufjf.dcc168.tictactoe.domain.Game;
import br.ufjf.dcc168.tictactoe.domain.Position;

/**
 * Sequências de jogadas da Tabela 2 da Parte I.
 *
 * <p>Cada item é {linha, coluna}, na ordem X, O, X, ... Nenhuma sequência termina antes da última
 * jogada. Os arrays não devem ser alterados pelos testes.
 */
public final class Moves {

    /** CT01, CT14, CT16: X completa a linha 0 na 5ª jogada. */
    public static final int[][] X_WINS_ROW_0 = {{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}};

    /** CT02: O completa a coluna 2 na 6ª jogada. */
    public static final int[][] O_WINS_COLUMN_2 = {{0, 0}, {0, 2}, {1, 0}, {1, 2}, {2, 1}, {2, 2}};

    /** CT03: X completa a diagonal principal. */
    public static final int[][] X_WINS_MAIN_DIAGONAL = {{0, 0}, {0, 1}, {1, 1}, {0, 2}, {2, 2}};

    /** CT04: X completa a diagonal secundária. */
    public static final int[][] X_WINS_ANTI_DIAGONAL = {{0, 2}, {0, 0}, {1, 1}, {0, 1}, {2, 0}};

    /** CT05, CT15: o tabuleiro fica cheio sem nenhuma linha completa. */
    public static final int[][] DRAW = {
        {0, 0}, {0, 1}, {0, 2}, {1, 1}, {1, 0}, {2, 0}, {2, 1}, {1, 2}, {2, 2}
    };

    /** CT06: X completa a diagonal principal na 9ª jogada, preenchendo o tabuleiro. */
    public static final int[][] X_WINS_ON_NINTH_MOVE = {
        {0, 2}, {0, 1}, {1, 1}, {2, 0}, {2, 1}, {1, 0}, {0, 0}, {1, 2}, {2, 2}
    };

    private Moves() {}

    /** Aplica as jogadas, em ordem, direto no domínio. */
    public static void playAll(Game game, int[][] moves) {
        for (int[] move : moves) {
            game.play(new Position(move[0], move[1]));
        }
    }

    /** Converte as jogadas nas linhas que o jogador digitaria: {0, 2} vira "0 2". */
    public static String[] asTypedLines(int[][] moves) {
        String[] lines = new String[moves.length];
        for (int i = 0; i < moves.length; i++) {
            lines[i] = moves[i][0] + " " + moves[i][1];
        }
        return lines;
    }
}
