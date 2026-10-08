package br.ufjf.dcc168.tictactoe.functional;

import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.I3;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.I4;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.I5;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.I6;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.I7;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.I8;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V1;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V11;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V12;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V2;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V3;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V4;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V5;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V8;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import br.ufjf.dcc168.tictactoe.domain.Game;
import br.ufjf.dcc168.tictactoe.domain.GameStatus;
import br.ufjf.dcc168.tictactoe.domain.InvalidMoveException;
import br.ufjf.dcc168.tictactoe.domain.InvalidMoveException.Reason;
import br.ufjf.dcc168.tictactoe.domain.Position;
import br.ufjf.dcc168.tictactoe.domain.Symbol;
import br.ufjf.dcc168.tictactoe.report.TestCase;
import br.ufjf.dcc168.tictactoe.support.Moves;

import org.junit.Test;

public class GameFunctionalTest {

    @TestCase(
            id = "CT03",
            input = "<X(0,0), O(0,1), X(1,1), O(0,2), X(2,2)>",
            expected = "X_WINS (diagonal principal)",
            classes = {V1, V2, V3, V4, V5, V8, V12})
    @Test
    public void ct03_xCompletesMainDiagonal_xWins() {
        Game game = new Game();
        Moves.playAll(game, Moves.X_WINS_MAIN_DIAGONAL);

        assertEquals(GameStatus.X_WINS, game.getStatus());
    }

    @TestCase(
            id = "CT04",
            input = "<X(0,2), O(0,0), X(1,1), O(0,1), X(2,0)>",
            expected = "X_WINS (diagonal secundária)",
            classes = {V1, V2, V3, V4, V5, V9, V12})
    @Test
    public void ct04_xCompletesAntiDiagonal_xWins() {
        Game game = new Game();
        Moves.playAll(game, Moves.X_WINS_ANTI_DIAGONAL);

        assertEquals(GameStatus.X_WINS, game.getStatus());
    }

    @TestCase(
            id = "CT06",
            input = "<X(0,2), O(0,1), X(1,1), O(2,0), X(2,1), O(1,0), X(0,0), O(1,2), X(2,2)>",
            expected = "X_WINS (diagonal principal na 9ª jogada), não DRAW",
            classes = {V1, V2, V3, V4, V5, V8, V12})
    @Test
    public void ct06_xCompletesLineOnNinthMove_xWinsNotDraw() {
        Game game = new Game();
        Moves.playAll(game, Moves.X_WINS_ON_NINTH_MOVE);

        assertEquals(GameStatus.X_WINS, game.getStatus());
        assertTrue(game.getBoard().isFull());
    }

    @TestCase(
            id = "CT07",
            input = "<X(1,1)>",
            expected = "IN_PROGRESS; X jogou primeiro; vez de O",
            classes = {V1, V2, V3, V4, V5, V11})
    @Test
    public void ct07_firstMove_isXAndTurnPassesToO() {
        Game game = new Game();
        play(game, 1, 1);

        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertEquals(Symbol.X, symbolAt(game, 1, 1));
        assertEquals(Symbol.O, game.getCurrentPlayer());
    }

    @TestCase(
            id = "CT08",
            input = "<X(0,0), O(0,2), X(2,0), O(2,2)>",
            expected = "IN_PROGRESS; as 4 jogadas aceitas; vez de X",
            classes = {V1, V2, V3, V4, V5, V11})
    @Test
    public void ct08_cornerMoves_allAcceptedAndTurnAlternates() {
        Game game = new Game();
        play(game, 0, 0);
        play(game, 0, 2);
        play(game, 2, 0);
        play(game, 2, 2);

        assertEquals(Symbol.X, symbolAt(game, 0, 0));
        assertEquals(Symbol.O, symbolAt(game, 0, 2));
        assertEquals(Symbol.X, symbolAt(game, 2, 0));
        assertEquals(Symbol.O, symbolAt(game, 2, 2));
        assertEquals(4, game.getBoard().countFilledCells());
        assertEquals(Symbol.X, game.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @TestCase(
            id = "CT09",
            input = "<X(−1,0)>",
            expected = "Rejeitada: POSITION_OUT_OF_BOUNDS; tabuleiro vazio; vez de X",
            classes = {V1, I3, V3})
    @Test
    public void ct09_rowMinusOne_rejectedOutOfBounds() {
        Game game = new Game();
        InvalidMoveException error =
                assertThrows(InvalidMoveException.class, () -> play(game, -1, 0));

        assertEquals(Reason.POSITION_OUT_OF_BOUNDS, error.getReason());
        assertGameUntouched(game);
    }

    @TestCase(
            id = "CT10",
            input = "<X(3,0)>",
            expected = "Rejeitada: POSITION_OUT_OF_BOUNDS; tabuleiro vazio; vez de X",
            classes = {V1, I4, V3})
    @Test
    public void ct10_rowThree_rejectedOutOfBounds() {
        Game game = new Game();
        InvalidMoveException error =
                assertThrows(InvalidMoveException.class, () -> play(game, 3, 0));

        assertEquals(Reason.POSITION_OUT_OF_BOUNDS, error.getReason());
        assertGameUntouched(game);
    }

    @TestCase(
            id = "CT11",
            input = "<X(0,−1)>",
            expected = "Rejeitada: POSITION_OUT_OF_BOUNDS; tabuleiro vazio; vez de X",
            classes = {V1, V2, I5})
    @Test
    public void ct11_columnMinusOne_rejectedOutOfBounds() {
        Game game = new Game();
        InvalidMoveException error =
                assertThrows(InvalidMoveException.class, () -> play(game, 0, -1));

        assertEquals(Reason.POSITION_OUT_OF_BOUNDS, error.getReason());
        assertGameUntouched(game);
    }

    @TestCase(
            id = "CT12",
            input = "<X(0,3)>",
            expected = "Rejeitada: POSITION_OUT_OF_BOUNDS; tabuleiro vazio; vez de X",
            classes = {V1, V2, I6})
    @Test
    public void ct12_columnThree_rejectedOutOfBounds() {
        Game game = new Game();
        InvalidMoveException error =
                assertThrows(InvalidMoveException.class, () -> play(game, 0, 3));

        assertEquals(Reason.POSITION_OUT_OF_BOUNDS, error.getReason());
        assertGameUntouched(game);
    }

    @TestCase(
            id = "CT13",
            input = "<X(0,0), O(0,0)>",
            expected = "2ª jogada rejeitada: CELL_OCCUPIED; célula (0,0) continua com X; vez de O",
            classes = {V1, V2, V3, I7, V5})
    @Test
    public void ct13_moveOnOccupiedCell_rejectedCellOccupied() {
        Game game = new Game();
        play(game, 0, 0);

        InvalidMoveException error =
                assertThrows(InvalidMoveException.class, () -> play(game, 0, 0));

        assertEquals(Reason.CELL_OCCUPIED, error.getReason());
        assertEquals(Symbol.X, symbolAt(game, 0, 0));
        assertEquals(1, game.getBoard().countFilledCells());
        assertEquals(Symbol.O, game.getCurrentPlayer());
    }

    @TestCase(
            id = "CT14",
            input = "jogadas do CT01 + <O(2,2)>",
            expected = "Rejeitada: GAME_ALREADY_FINISHED; resultado continua X_WINS",
            classes = {V1, V2, V3, V4, I8})
    @Test
    public void ct14_moveAfterWin_rejectedGameFinished() {
        Game game = new Game();
        Moves.playAll(game, Moves.X_WINS_ROW_0);

        InvalidMoveException error =
                assertThrows(InvalidMoveException.class, () -> play(game, 2, 2));

        assertEquals(Reason.GAME_ALREADY_FINISHED, error.getReason());
        assertEquals(GameStatus.X_WINS, game.getStatus());
        assertNull(symbolAt(game, 2, 2));
    }

    @TestCase(
            id = "CT15",
            input = "jogadas do CT05 + <X(0,0)>",
            expected =
                    "Rejeitada: GAME_ALREADY_FINISHED (não CELL_OCCUPIED); resultado continua DRAW",
            classes = {V1, V2, V3, I7, I8})
    @Test
    public void ct15_occupiedCellAfterDraw_rejectedGameFinished() {
        Game game = new Game();
        Moves.playAll(game, Moves.DRAW);

        InvalidMoveException error =
                assertThrows(InvalidMoveException.class, () -> play(game, 0, 0));

        assertEquals(Reason.GAME_ALREADY_FINISHED, error.getReason());
        assertEquals(GameStatus.DRAW, game.getStatus());
    }

    @TestCase(
            id = "CT16",
            input = "jogadas do CT01 + <O(3,3)>",
            expected =
                    "Rejeitada: POSITION_OUT_OF_BOUNDS (não GAME_ALREADY_FINISHED); resultado"
                            + " continua X_WINS",
            classes = {V1, I4, I6, I8})
    @Test
    public void ct16_outOfBoundsAfterWin_rejectedOutOfBounds() {
        Game game = new Game();
        Moves.playAll(game, Moves.X_WINS_ROW_0);

        InvalidMoveException error =
                assertThrows(InvalidMoveException.class, () -> play(game, 3, 3));

        assertEquals(Reason.POSITION_OUT_OF_BOUNDS, error.getReason());
        assertEquals(GameStatus.X_WINS, game.getStatus());
    }

    // O new Position(...) fica aqui dentro: é ele que rejeita posições fora do tabuleiro.
    private void play(Game game, int row, int column) {
        game.play(new Position(row, column));
    }

    private Symbol symbolAt(Game game, int row, int column) {
        return game.getBoard().getSymbolAt(new Position(row, column));
    }

    private void assertGameUntouched(Game game) {
        assertEquals(0, game.getBoard().countFilledCells());
        assertEquals(Symbol.X, game.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
    }
}
