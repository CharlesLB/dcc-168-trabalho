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
}
