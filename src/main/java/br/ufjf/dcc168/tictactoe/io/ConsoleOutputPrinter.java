package br.ufjf.dcc168.tictactoe.io;

import java.io.PrintStream;

/** Escreve em um PrintStream, por padrão a tela (System.out). */
public class ConsoleOutputPrinter implements OutputPrinter {

    private final PrintStream target;

    public ConsoleOutputPrinter() {
        this(System.out);
    }

    public ConsoleOutputPrinter(PrintStream target) {
        this.target = target;
    }

    @Override
    public void printLine(String text) {
        target.println(text);
    }
}
