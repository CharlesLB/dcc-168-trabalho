package br.ufjf.dcc168.tictactoe.structural;

import static org.junit.Assert.assertEquals;

import br.ufjf.dcc168.tictactoe.Main;
import br.ufjf.dcc168.tictactoe.domain.report.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * TestSet-Estr (Parte II-B): {@code Main} é o único ponto do programa ligado a {@code System.in} e
 * {@code System.out}. Para executá-lo, o caso troca os dois por fluxos em memória e os devolve no
 * fim.
 */
public class MainStructuralTest {

    private InputStream originalInput;
    private PrintStream originalOutput;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Before
    public void redirectStandardStreams() throws Exception {
        originalInput = System.in;
        originalOutput = System.out;
        System.setOut(new PrintStream(output, true, "UTF-8"));
    }

    @After
    public void restoreStandardStreams() {
        System.setIn(originalInput);
        System.setOut(originalOutput);
    }

    @TestCase(
            id = "CE05",
            input = "Main.main com a entrada padrão \"0 0\\n1 0\\n0 1\\n1 1\\n0 2\\n\"",
            expected = "Última linha da saída padrão: \"X venceu!\"",
            requirement =
                    "Main.main (linhas 13 a 15) e os construtores sem argumento de"
                            + " ConsoleInputReader e ConsoleOutputPrinter (linhas 13 e 10):"
                            + " nós não executados pelo TestSet-Func")
    @Test
    public void ce05_mainWithXWinningGame_printsXWins() throws Exception {
        System.setIn(
                new ByteArrayInputStream(
                        "0 0\n1 0\n0 1\n1 1\n0 2\n".getBytes(StandardCharsets.UTF_8)));

        Main.main(new String[0]);

        String[] lines = output.toString("UTF-8").split("\\R");
        assertEquals("X venceu!", lines[lines.length - 1]);
    }
}
