package br.ufjf.dcc168.tictactoe.mutation;

import static org.junit.Assert.assertTrue;

import br.ufjf.dcc168.tictactoe.console.ConsoleGame;
import br.ufjf.dcc168.tictactoe.domain.Game;
import br.ufjf.dcc168.tictactoe.report.TestCase;
import br.ufjf.dcc168.tictactoe.support.RecordingOutputPrinter;
import br.ufjf.dcc168.tictactoe.support.ScriptedInputReader;

import org.junit.Test;

/**
 * Casos adicionados na Parte III-B para matar mutantes que sobreviveram ao TestSet-Func e ao
 * TestSet-Estr.
 *
 * <p>Convenção de nome: cm{ID}_{cenario}_{resultadoEsperado}. Registrar no comentário de cada teste
 * o mutante alvo (classe, linha e operador do PITest).
 *
 * <p>Os mutantes que continuam vivos são equivalentes; a justificativa está em
 * docs/mutantes-equivalentes.md.
 */
public class GameMutationTest {

    // Mutantes alvo (nenhum caso anterior confere o texto de uma InvalidMoveException):
    // - InvalidMoveException$Reason, linha 24, EmptyObjectReturnValsMutator: getMessage retorna "";
    // - InvalidMoveException, linha 31, NonVoidMethodCallMutator: o construtor passa null ao
    //   super no lugar de reason.getMessage().
    @TestCase(
            id = "CM01",
            input = "<\"3 0\">",
            expected =
                    "Mensagem \"Jogada inválida: posição fora do tabuleiro (use valores de 0 a"
                            + " 2)\"",
            requirement =
                    "InvalidMoveException linha 24 (getMessage retorna \"\") e linha 31 (chamada"
                            + " a getMessage removida)")
    @Test
    public void cm01_outOfBoundsOnConsole_showsReasonMessage() {
        RecordingOutputPrinter printer = new RecordingOutputPrinter();
        new ConsoleGame(new Game(), new ScriptedInputReader("3 0"), printer).run();

        assertTrue(
                printer.getLines()
                        .contains(
                                "Jogada inválida: posição fora do tabuleiro (use valores de 0 a"
                                        + " 2)"));
    }
}
