package br.ufjf.dcc168.tictactoe.domain;

/**
 * Lançada quando uma jogada viola as regras.
 *
 * <p>Cada motivo corresponde a uma classe de equivalência inválida da Parte I, o que facilita a
 * rastreabilidade entre a tabela de casos de teste e o código.
 */
public class InvalidMoveException extends RuntimeException {

    public enum Reason {
        POSITION_OUT_OF_BOUNDS,
        CELL_OCCUPIED,
        GAME_ALREADY_FINISHED
    }

    private final Reason reason;

    public InvalidMoveException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
