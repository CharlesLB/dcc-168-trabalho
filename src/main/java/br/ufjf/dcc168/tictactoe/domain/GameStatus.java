package br.ufjf.dcc168.tictactoe.domain;

public enum GameStatus {
    IN_PROGRESS,
    X_WINS,
    O_WINS,
    DRAW;

    public boolean isFinished() {
        return this != IN_PROGRESS;
    }

    public static GameStatus victoryOf(Symbol winner) {
        return winner == Symbol.X ? X_WINS : O_WINS;
    }
}
