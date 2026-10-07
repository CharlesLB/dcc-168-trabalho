package br.ufjf.dcc168.tictactoe.domain;

/**
 * Partida de Jogo da Velha: controla turnos e decide o resultado.
 *
 * <p>Regras definidas pelo grupo: - X sempre começa; - jogadas após o fim da partida são
 * rejeitadas; - a vitória é verificada antes do empate (vitória na 9ª jogada é vitória).
 */
public class Game {

    private final Board board = new Board();
    private Symbol currentPlayer = Symbol.X;
    private GameStatus status = GameStatus.IN_PROGRESS;

    /**
     * Executa a jogada do jogador da vez e passa o turno.
     *
     * @throws InvalidMoveException se a partida já terminou, a posição for inválida ou a célula
     *     estiver ocupada
     */
    public void play(Position position) {
        // TODO:
        //  1. rejeitar se a partida já terminou
        //  2. colocar o símbolo no tabuleiro
        //  3. atualizar o status (vitória antes de empate)
        //  4. passar o turno, se a partida continuar
        throw new UnsupportedOperationException("TODO");
    }

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
