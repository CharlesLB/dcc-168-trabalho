package br.ufjf.dcc168.tictactoe.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class ConsoleInputReaderTest {

    @Test
    public void readLine_returnsLinesInOrder() {
        InputReader reader = readerFor("0 0\n1 2\n");

        assertEquals("0 0", reader.readLine());
        assertEquals("1 2", reader.readLine());
    }

    @Test(expected = InputExhaustedException.class)
    public void readLine_withoutInput_throwsInputExhausted() {
        readerFor("").readLine();
    }

    private InputReader readerFor(String text) {
        return new ConsoleInputReader(
                new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    }
}
