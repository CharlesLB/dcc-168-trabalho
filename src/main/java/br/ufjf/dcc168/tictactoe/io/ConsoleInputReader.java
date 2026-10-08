package br.ufjf.dcc168.tictactoe.io;

import br.ufjf.dcc168.tictactoe.io.exception.InputExhaustedException;

import java.io.InputStream;
import java.util.Scanner;

public class ConsoleInputReader implements InputReader {

    private final Scanner scanner;

    public ConsoleInputReader() {
        this(System.in);
    }

    public ConsoleInputReader(InputStream source) {
        this.scanner = new Scanner(source, "UTF-8");
    }

    @Override
    public String readLine() {
        if (!scanner.hasNextLine()) {
            throw new InputExhaustedException();
        }
        return scanner.nextLine();
    }
}
