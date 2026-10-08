package br.ufjf.dcc168.tictactoe.domain;

import br.ufjf.dcc168.tictactoe.domain.InvalidMoveException.Reason;

public class Game {

    private final Board board = new Board();
    private Symbol currentPlayer = Symbol.X;
    private GameStatus status = GameStatus.IN_PROGRESS;

    public void play(Position position) {
        if (status.isFinished()) {
            throw new InvalidMoveException(Reason.GAME_ALREADY_FINISHED);
        }
        board.place(currentPlayer, position);

        // D9: a vitória é verificada antes do empate, então vencer na 9ª jogada é vitória.
        if (board.hasCompleteLine(currentPlayer)) {
            status = GameStatus.victoryOf(currentPlayer);
        } else if (board.isFull()) {
            status = GameStatus.DRAW;
        } else {
            currentPlayer = currentPlayer.opponent();
        }
    }

    /** Depois do fim da partida, continua sendo quem fez a última jogada. */
    public Symbol getCurrentPlayer() {
        return currentPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Board getBoard() {
        return board;
    }
}
