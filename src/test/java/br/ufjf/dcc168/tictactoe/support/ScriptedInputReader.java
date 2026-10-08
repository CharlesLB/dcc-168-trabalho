package br.ufjf.dcc168.tictactoe.support;

import br.ufjf.dcc168.tictactoe.io.InputExhaustedException;
import br.ufjf.dcc168.tictactoe.io.InputReader;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class ScriptedInputReader implements InputReader {

    private final Deque<String> remainingLines;

    public ScriptedInputReader(String... lines) {
        this.remainingLines = new ArrayDeque<>(Arrays.asList(lines));
    }

    @Override
    public String readLine() {
        if (remainingLines.isEmpty()) {
            throw new InputExhaustedException();
        }
        return remainingLines.poll();
    }
}
