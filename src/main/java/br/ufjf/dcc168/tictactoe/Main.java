package br.ufjf.dcc168.tictactoe;

import br.ufjf.dcc168.tictactoe.console.ConsoleGame;
import br.ufjf.dcc168.tictactoe.domain.Game;
import br.ufjf.dcc168.tictactoe.io.ConsoleInputReader;
import br.ufjf.dcc168.tictactoe.io.ConsoleOutputPrinter;

/** Ponto de entrada: único lugar que conhece o console real. */
public class Main {

    public static void main(String[] args) {
        ConsoleGame consoleGame =
                new ConsoleGame(new Game(), new ConsoleInputReader(), new ConsoleOutputPrinter());
        consoleGame.run();
    }
}
