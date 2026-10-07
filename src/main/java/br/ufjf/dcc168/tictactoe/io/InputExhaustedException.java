package br.ufjf.dcc168.tictactoe.io;

/** Lançada quando o leitor não tem mais linhas para fornecer. */
public class InputExhaustedException extends RuntimeException {

    public InputExhaustedException() {
        super("No more input available");
    }
}
