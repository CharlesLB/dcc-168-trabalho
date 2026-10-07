package br.ufjf.dcc168.tictactoe.domain;

/** Situação atual da partida. */
public enum GameStatus {
    IN_PROGRESS,
    X_WINS,
    O_WINS,
    DRAW;

    /** Indica se a partida já terminou (vitória ou empate). */
    public boolean isFinished() {
        return this != IN_PROGRESS;
    }

    /** Status de vitória do jogador informado: X → X_WINS, O → O_WINS. */
    public static GameStatus victoryOf(Symbol winner) {
        return winner == Symbol.X ? X_WINS : O_WINS;
    }
}
