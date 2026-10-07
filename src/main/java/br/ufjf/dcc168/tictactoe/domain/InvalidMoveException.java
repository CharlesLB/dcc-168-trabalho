package br.ufjf.dcc168.tictactoe.domain;

/**
 * Lançada quando uma jogada viola as regras.
 *
 * <p>Cada motivo corresponde a uma classe de equivalência inválida da Parte I, o que facilita a
 * rastreabilidade entre a tabela de casos de teste e o código. A mensagem de cada motivo é fixa e é
 * a que o console exibe.
 */
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
