package br.ufjf.dcc168.tictactoe.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TableTest {

    @Test
    public void toMarkdown_writesHeaderSeparatorAndRows() {
        Table table = new Table("Título", "ID", "Saída");
        table.addRow("CT01", "X_WINS");

        String expected = "**Título**\n\n| ID | Saída |\n|---|---|\n| CT01 | X_WINS |\n";
        assertEquals(expected, table.toMarkdown());
    }

    @Test
    public void toMarkdown_escapesPipeInsideCell() {
        Table table = new Table("Título", "Entrada");
        table.addRow("a | b");

        assertTrue(table.toMarkdown().contains("| a \\| b |"));
    }

    @Test
    public void toMarkdown_escapesAngleBracketOfInputNotation() {
        Table table = new Table("Título", "Entrada");
        table.addRow("<0, 0>");

        assertTrue(table.toMarkdown().contains("| \\<0, 0> |"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRow_withWrongNumberOfCells_isRejected() {
        new Table("Título", "ID", "Saída").addRow("CT01");
    }
}
