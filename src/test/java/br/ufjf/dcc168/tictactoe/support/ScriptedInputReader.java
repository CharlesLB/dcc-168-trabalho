package br.ufjf.dcc168.tictactoe.support;

import br.ufjf.dcc168.tictactoe.io.InputExhaustedException;
import br.ufjf.dcc168.tictactoe.io.InputReader;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Leitor de teste: devolve linhas pré-definidas, na ordem, como se fossem digitadas.
 *
 * <p>Uso: {@code new ScriptedInputReader("0 0", "1 1", "0 1")}
 */
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
