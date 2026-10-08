package br.ufjf.dcc168.tictactoe.domain;

public class InvalidMoveException extends RuntimeException {

    public enum Reason {
        POSITION_OUT_OF_BOUNDS("posição fora do tabuleiro (use valores de 0 a 2)"),
        CELL_OCCUPIED("célula já ocupada"),
        GAME_ALREADY_FINISHED("a partida já terminou");

        private final String message;

        Reason(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private final Reason reason;

    public InvalidMoveException(Reason reason) {
        super(reason.getMessage());
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
