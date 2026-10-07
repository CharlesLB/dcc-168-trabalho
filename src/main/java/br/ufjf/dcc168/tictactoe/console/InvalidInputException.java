package br.ufjf.dcc168.tictactoe.console;

/** Lançada quando o texto digitado não tem o formato esperado. */
public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }
}
