package br.ufjf.dcc168.tictactoe.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ConsoleOutputPrinterTest {

    @Test
    public void printLine_writesTextFollowedByLineBreak() throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        OutputPrinter printer = new ConsoleOutputPrinter(new PrintStream(buffer, true, "UTF-8"));

        printer.printLine("Vez de X");
        printer.printEmptyLine();

        String lineBreak = System.lineSeparator();
        assertEquals("Vez de X" + lineBreak + lineBreak, buffer.toString("UTF-8"));
    }
}
