package br.ufjf.dcc168.tictactoe.structural;

import static org.junit.Assert.assertEquals;

import br.ufjf.dcc168.tictactoe.domain.Position;
import br.ufjf.dcc168.tictactoe.domain.report.TestCase;

import org.junit.Test;

/**
 * TestSet-Estr (Parte II-B): {@code Position.toString} é o único método de {@code domain} que o
 * TestSet-Func não executa, porque nenhum caso da Parte I mostra uma posição na tela.
 */
public class PositionStructuralTest {

    @TestCase(
            id = "CE01",
            input = "Position(1, 2).toString()",
            expected = "\"(1, 2)\"",
            requirement = "Position.toString (linha 33): nós não executados pelo TestSet-Func")
    @Test
    public void ce01_positionToString_formatsRowAndColumn() {
        assertEquals("(1, 2)", new Position(1, 2).toString());
    }
}
