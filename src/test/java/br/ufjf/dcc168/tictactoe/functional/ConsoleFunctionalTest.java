package br.ufjf.dcc168.tictactoe.functional;

import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.I1;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.I2;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V1;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V10;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V11;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V12;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V13;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V2;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V3;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V4;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V5;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V6;
import static br.ufjf.dcc168.tictactoe.specification.EquivalenceClass.V7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import br.ufjf.dcc168.tictactoe.console.ConsoleGame;
import br.ufjf.dcc168.tictactoe.domain.Game;
import br.ufjf.dcc168.tictactoe.domain.GameStatus;
import br.ufjf.dcc168.tictactoe.domain.Position;
import br.ufjf.dcc168.tictactoe.domain.Symbol;
import br.ufjf.dcc168.tictactoe.report.TestCase;
import br.ufjf.dcc168.tictactoe.support.Moves;
import br.ufjf.dcc168.tictactoe.support.RecordingOutputPrinter;
import br.ufjf.dcc168.tictactoe.support.ScriptedInputReader;

import org.junit.Test;

/**
 * Nos casos em que a partida não termina, o roteiro acaba e o ConsoleGame encerra normalmente, sem
 * propagar exceção.
 */
public class ConsoleFunctionalTest {

    private static final String INVALID_MOVE_PREFIX = "Jogada inválida";

    private final Game game = new Game();
    private final RecordingOutputPrinter printer = new RecordingOutputPrinter();

    @TestCase(
            id = "CT01",
            input = "<X(0,0), O(1,0), X(0,1), O(1,1), X(0,2)>",
            expected = "X_WINS (linha 0); mensagem \"X venceu!\"",
            classes = {V1, V2, V3, V4, V5, V6, V12})
    @Test
    public void ct01_xCompletesRow0_xWins() {
        runConsoleWith(Moves.asTypedLines(Moves.X_WINS_ROW_0));

        assertEquals(GameStatus.X_WINS, game.getStatus());
        assertEquals("X venceu!", printer.getLastLine());
        assertFalse(printer.hasLineContaining(INVALID_MOVE_PREFIX));
    }

    @TestCase(
            id = "CT02",
            input = "<X(0,0), O(0,2), X(1,0), O(1,2), X(2,1), O(2,2)>",
            expected = "O_WINS (coluna 2); mensagem \"O venceu!\"",
            classes = {V1, V2, V3, V4, V5, V7, V13})
    @Test
    public void ct02_oCompletesColumn2_oWins() {
        runConsoleWith(Moves.asTypedLines(Moves.O_WINS_COLUMN_2));

        assertEquals(GameStatus.O_WINS, game.getStatus());
        assertEquals("O venceu!", printer.getLastLine());
        assertFalse(printer.hasLineContaining(INVALID_MOVE_PREFIX));
    }

    @TestCase(
            id = "CT05",
            input = "<X(0,0), O(0,1), X(0,2), O(1,1), X(1,0), O(2,0), X(2,1), O(1,2), X(2,2)>",
            expected = "DRAW; mensagem \"Empate!\"",
            classes = {V1, V2, V3, V4, V5, V10})
    @Test
    public void ct05_boardFullWithoutLine_draw() {
        runConsoleWith(Moves.asTypedLines(Moves.DRAW));

        assertEquals(GameStatus.DRAW, game.getStatus());
        assertEquals("Empate!", printer.getLastLine());
        assertFalse(printer.hasLineContaining(INVALID_MOVE_PREFIX));
    }

    @TestCase(
            id = "CT17",
            input = "<\"a 1\">",
            expected =
                    "Mensagem \"Jogada inválida: 'a' não é um número\"; tabuleiro vazio; vez de X",
            classes = {I2})
    @Test
    public void ct17_nonNumericValue_showsNotANumberMessage() {
        runConsoleWith("a 1");

        assertPrinted("Jogada inválida: 'a' não é um número");
        assertGameUntouched();
    }

    @TestCase(
            id = "CT18",
            input = "<\"1\">",
            expected =
                    "Mensagem \"Jogada inválida: informe exatamente dois números\"; tabuleiro"
                            + " vazio; vez de X",
            classes = {I1})
    @Test
    public void ct18_singleValue_showsTwoNumbersMessage() {
        runConsoleWith("1");

        assertPrinted("Jogada inválida: informe exatamente dois números");
        assertGameUntouched();
    }

    @TestCase(
            id = "CT19",
            input = "<\"1 1 1\">",
            expected =
                    "Mensagem \"Jogada inválida: informe exatamente dois números\"; tabuleiro"
                            + " vazio; vez de X",
            classes = {I1})
    @Test
    public void ct19_threeValues_showsTwoNumbersMessage() {
        runConsoleWith("1 1 1");

        assertPrinted("Jogada inválida: informe exatamente dois números");
        assertGameUntouched();
    }

    @TestCase(
            id = "CT20",
            input = "<\"\"> (linha vazia)",
            expected =
                    "Mensagem \"Jogada inválida: informe exatamente dois números\"; tabuleiro"
                            + " vazio; vez de X",
            classes = {I1})
    @Test
    public void ct20_emptyLine_showsTwoNumbersMessage() {
        runConsoleWith("");

        assertPrinted("Jogada inválida: informe exatamente dois números");
        assertGameUntouched();
    }

    @TestCase(
            id = "CT21",
            input = "<\"  1   1  \"> (espaços extras)",
            expected = "Jogada aceita em (1,1); IN_PROGRESS; vez de O",
            classes = {V1, V2, V3, V4, V5, V11})
    @Test
    public void ct21_extraSpaces_moveAccepted() {
        runConsoleWith("  1   1  ");

        assertEquals(Symbol.X, game.getBoard().getSymbolAt(new Position(1, 1)));
        assertEquals(Symbol.O, game.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertFalse(printer.hasLineContaining(INVALID_MOVE_PREFIX));
    }

    private void runConsoleWith(String... typedLines) {
        new ConsoleGame(game, new ScriptedInputReader(typedLines), printer).run();
    }

    private void assertPrinted(String expectedLine) {
        assertTrue(
                "Linha não impressa: " + expectedLine, printer.getLines().contains(expectedLine));
    }

    private void assertGameUntouched() {
        assertEquals(0, game.getBoard().countFilledCells());
        assertEquals(Symbol.X, game.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
    }
}
