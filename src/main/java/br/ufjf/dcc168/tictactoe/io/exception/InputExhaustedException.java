package br.ufjf.dcc168.tictactoe.io.exception;

public class InputExhaustedException extends RuntimeException {

    public InputExhaustedException() {
        super("No more input available");
    }
}
