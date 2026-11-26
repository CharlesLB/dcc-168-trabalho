package br.ufjf.dcc168.tictactoe.structural;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import br.ufjf.dcc168.tictactoe.domain.report.TestCase;
import br.ufjf.dcc168.tictactoe.io.ConsoleInputReader;
import br.ufjf.dcc168.tictactoe.io.ConsoleOutputPrinter;
import br.ufjf.dcc168.tictactoe.io.InputReader;
import br.ufjf.dcc168.tictactoe.io.OutputPrinter;
import br.ufjf.dcc168.tictactoe.io.exception.InputExhaustedException;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * TestSet-Estr (Parte II-B): o TestSet-Func roda o console com dublês ({@code ScriptedInputReader}
 * e {@code RecordingOutputPrinter}), então as implementações reais de {@code io} nunca executam.
 * Estes casos usam fluxos em memória no lugar do teclado e da tela.
 */
public class ConsoleIoStructuralTest {

    @TestCase(
            id = "CE02",
            input = "ConsoleInputReader sobre o texto \"0 0\\n1 2\\n\"; readLine() duas vezes",
            expected = "\"0 0\" e depois \"1 2\"",
            requirement =
                    "ConsoleInputReader.readLine: ramo falso de !hasNextLine() (linha 22 para a"
                            + " 25) e pares def-uso de this.scanner: definição na linha 22, usos"
                            + " em (22, 25) e na linha 25")
    @Test
    public void ce02_readerWithLines_returnsLinesInOrder() {
        InputReader reader = readerFor("0 0\n1 2\n");

        assertEquals("0 0", reader.readLine());
        assertEquals("1 2", reader.readLine());
    }

    @TestCase(
            id = "CE03",
            input = "ConsoleInputReader sobre o texto vazio; readLine()",
            expected = "InputExhaustedException",
            requirement =
                    "ConsoleInputReader.readLine: ramo verdadeiro de !hasNextLine() (linha 22"
                            + " para a 23) e par def-uso de this.scanner: definição na linha 22,"
                            + " uso em (22, 23)")
    @Test
    public void ce03_readerWithoutInput_throwsInputExhausted() {
        InputReader reader = readerFor("");

        assertThrows(InputExhaustedException.class, reader::readLine);
    }

    @TestCase(
            id = "CE04",
            input = "ConsoleOutputPrinter sobre um fluxo em memória; printLine(\"Vez de X\")",
            expected = "\"Vez de X\" seguido de quebra de linha",
            requirement =
                    "ConsoleOutputPrinter(PrintStream) e printLine (linhas 14 e 19): nós não"
                            + " executados pelo TestSet-Func")
    @Test
    public void ce04_printerWritesLine_textFollowedByLineBreak() throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        OutputPrinter printer = new ConsoleOutputPrinter(new PrintStream(buffer, true, "UTF-8"));

        printer.printLine("Vez de X");

        assertEquals("Vez de X" + System.lineSeparator(), buffer.toString("UTF-8"));
    }

    private static InputReader readerFor(String text) {
        return new ConsoleInputReader(
                new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    }
}
