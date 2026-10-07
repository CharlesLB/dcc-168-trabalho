package br.ufjf.dcc168.tictactoe.console;

import br.ufjf.dcc168.tictactoe.domain.Game;
import br.ufjf.dcc168.tictactoe.domain.GameStatus;
import br.ufjf.dcc168.tictactoe.domain.InvalidMoveException;
import br.ufjf.dcc168.tictactoe.io.InputReader;
import br.ufjf.dcc168.tictactoe.io.OutputPrinter;

/**
 * Laço de interação com os jogadores.
 *
 * <p>Toda a comunicação passa por InputReader e OutputPrinter, então esta classe pode ser testada
 * com entradas roteirizadas, sem teclado nem tela.
 */
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

    /** Executa a partida até haver vitória ou empate. */
    public void run() {
        printer.printLine("Jogo da Velha");
        while (!game.getStatus().isFinished()) {
            renderer.render(game.getBoard());
            playOneTurn();
        }
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
