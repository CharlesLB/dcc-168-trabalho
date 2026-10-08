package br.ufjf.dcc168.tictactoe.console;

import br.ufjf.dcc168.tictactoe.console.exception.InvalidInputException;
import br.ufjf.dcc168.tictactoe.domain.Position;

public final class MoveParser {

    private MoveParser() {}

    public static Position parse(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length != 2) {
            throw new InvalidInputException("informe exatamente dois números");
        }
        return new Position(toInt(parts[0]), toInt(parts[1]));
    }

    private static int toInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException error) {
            throw new InvalidInputException("'" + text + "' não é um número");
        }
    }
}
