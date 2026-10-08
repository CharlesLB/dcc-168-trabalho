package br.ufjf.dcc168.tictactoe.structural;

import static org.junit.Assert.assertEquals;

import br.ufjf.dcc168.tictactoe.domain.Position;
import br.ufjf.dcc168.tictactoe.report.TestCase;

import org.junit.Test;

/**
 * Partindo do TestSet-Func, o único requisito factível não coberto em {@code domain} e {@code
 * console} é o método {@code Position.toString}. Os pares def-uso que continuam sem cobertura são
 * infactíveis; a justificativa está em docs/cobertura-estrutural.md.
 */
public class GameStructuralTest {

    // Requisito: nós (instruções) da linha 35 de Position.toString, o único método que o
    // TestSet-Func não executa. O método não tem ramos nem pares def-uso.
    @TestCase(
            id = "CE01",
            input = "Position(1, 2).toString()",
            expected = "\"(1, 2)\"",
            requirement = "Position.toString (linha 35): nós não executados pelo TestSet-Func")
    @Test
    public void ce01_positionToString_formatsRowAndColumn() {
        assertEquals("(1, 2)", new Position(1, 2).toString());
    }
}
