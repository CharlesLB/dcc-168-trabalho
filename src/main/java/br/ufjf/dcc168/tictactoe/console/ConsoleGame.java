package br.ufjf.dcc168.tictactoe.console;

import br.ufjf.dcc168.tictactoe.domain.Game;
import br.ufjf.dcc168.tictactoe.domain.GameStatus;
import br.ufjf.dcc168.tictactoe.domain.InvalidMoveException;
import br.ufjf.dcc168.tictactoe.io.InputExhaustedException;
import br.ufjf.dcc168.tictactoe.io.InputReader;
import br.ufjf.dcc168.tictactoe.io.OutputPrinter;

public class ConsoleGame {

    private final Game game;
    private final InputReader reader;
    private final OutputPrinter printer;
    private final BoardRenderer renderer;

    public ConsoleGame(Game game, InputReader reader, OutputPrinter printer) {
        this.game = game;
        this.reader = reader;
        this.printer = printer;
        this.renderer = new BoardRenderer(printer);
    }

    public void run() {
        printer.printLine("Jogo da Velha");
        try {
            playUntilFinished();
            showResult();
        } catch (InputExhaustedException endOfInput) {
            printer.printLine("Entrada encerrada. Partida interrompida.");
        }
    }

    private void playUntilFinished() {
        while (!game.getStatus().isFinished()) {
            renderer.render(game.getBoard());
            playOneTurn();
        }
    }

    private void showResult() {
        renderer.render(game.getBoard());
        printer.printLine(describeResult(game.getStatus()));
    }

    private void playOneTurn() {
        printer.printLine(
                "Vez de " + game.getCurrentPlayer() + ". Digite linha e coluna (ex.: 0 2):");
        String input = reader.readLine();
        try {
            game.play(MoveParser.parse(input));
        } catch (InvalidInputException | InvalidMoveException error) {
            printer.printLine("Jogada inválida: " + error.getMessage());
        }
    }

    private String describeResult(GameStatus status) {
        switch (status) {
            case X_WINS:
                return "X venceu!";
            case O_WINS:
                return "O venceu!";
            default:
                return "Empate!";
        }
    }
}
