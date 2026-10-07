package br.ufjf.dcc168.tictactoe.domain;

import br.ufjf.dcc168.tictactoe.domain.InvalidMoveException.Reason;

/**
 * Partida de Jogo da Velha: controla turnos e decide o resultado.
 *
 * <p>Regras definidas pelo grupo (Parte I):
 *
 * <ul>
 *   <li>X sempre começa (D1);
 *   <li>jogada rejeitada não altera tabuleiro, status nem jogador da vez (D4–D7);
 *   <li>jogadas após o fim da partida são rejeitadas (D7);
 *   <li>a vitória é verificada antes do empate: vitória na 9ª jogada é vitória (D9).
 * </ul>
 */
public class Game {

    private final Board board = new Board();
    private Symbol currentPlayer = Symbol.X;
    private GameStatus status = GameStatus.IN_PROGRESS;

    /**
     * Executa a jogada do jogador da vez e, se a partida continuar, passa a vez ao adversário.
     *
     * <p>A posição já chega válida: a checagem de limites acontece em {@link Position}.
     *
     * @throws InvalidMoveException se a partida já terminou (GAME_ALREADY_FINISHED) ou se a célula
     *     estiver ocupada (CELL_OCCUPIED)
     */
    public void play(Position position) {
        if (status.isFinished()) {
            throw new InvalidMoveException(Reason.GAME_ALREADY_FINISHED);
        }
        board.place(currentPlayer, position);

        if (board.hasCompleteLine(currentPlayer)) {
            status = GameStatus.victoryOf(currentPlayer);
        } else if (board.isFull()) {
            status = GameStatus.DRAW;
        } else {
            currentPlayer = currentPlayer.opponent();
        }
    }

    /** Jogador da vez. Depois do fim da partida, continua sendo quem fez a última jogada. */
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
