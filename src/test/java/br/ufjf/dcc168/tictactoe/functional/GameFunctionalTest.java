package br.ufjf.dcc168.tictactoe.functional;

import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.I5;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V1;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V2;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V3;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V4;

import static org.junit.Assert.assertEquals;

import br.ufjf.dcc168.tictactoe.domain.Game;
import br.ufjf.dcc168.tictactoe.domain.GameStatus;
import br.ufjf.dcc168.tictactoe.domain.InvalidMoveException;
import br.ufjf.dcc168.tictactoe.domain.Position;
import br.ufjf.dcc168.tictactoe.report.TestCase;

import org.junit.Ignore;
import org.junit.Test;

/**
 * TestSet-Func: casos de teste derivados da especificação (Parte I).
 *
 * <p>Cada método tem um @TestCase, que vira uma linha da Tabela 2 no ReportTablesGenerator.
 * Convenção de nome: ct{ID}_{cenario}_{resultadoEsperado}.
 */
public class GameFunctionalTest {

    // Exemplos do padrão. Remover os @Ignore quando Game.play estiver implementado.

    @TestCase(
            id = "CT01",
            input = "<X:(0,0), O:(1,0), X:(0,1), O:(1,1), X:(0,2)>",
            expected = "X_WINS",
            classes = {V1, V2, V3, V4})
    @Ignore("Game.play ainda não implementado")
    @Test
    public void ct01_xCompletesTopRow_xWins() {
        Game game = new Game();

        play(game, 0, 0); // X
        play(game, 1, 0); // O
        play(game, 0, 1); // X
        play(game, 1, 1); // O
        play(game, 0, 2); // X completa a linha 0

        assertEquals(GameStatus.X_WINS, game.getStatus());
    }

    @TestCase(
            id = "CT02",
            input = "<X:(0,0), O:(0,0)>",
            expected = "InvalidMoveException (CELL_OCCUPIED)",
            classes = {V1, V2, I5, V4})
    @Ignore("Game.play ainda não implementado")
    @Test
    public void ct02_moveOnOccupiedCell_throwsInvalidMove() {
        Game game = new Game();
        play(game, 0, 0); // X

        try {
            play(game, 0, 0); // O tenta a mesma célula
        } catch (InvalidMoveException error) {
            assertEquals(InvalidMoveException.Reason.CELL_OCCUPIED, error.getReason());
            return;
        }
        throw new AssertionError("Era esperada InvalidMoveException");
    }

    private void play(Game game, int row, int column) {
        game.play(new Position(row, column));
    }
}
