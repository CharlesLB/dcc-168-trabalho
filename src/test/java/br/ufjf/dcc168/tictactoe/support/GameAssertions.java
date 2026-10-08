package br.ufjf.dcc168.tictactoe.support;

import static org.junit.Assert.assertEquals;

import br.ufjf.dcc168.tictactoe.domain.Game;
import br.ufjf.dcc168.tictactoe.domain.GameStatus;
import br.ufjf.dcc168.tictactoe.domain.Symbol;

public final class GameAssertions {

    private GameAssertions() {}

    /** Tabuleiro vazio, vez de X e partida em andamento, como no início. */
    public static void assertGameUntouched(Game game) {
        assertEquals(0, game.getBoard().countFilledCells());
        assertEquals(Symbol.X, game.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
    }
}
