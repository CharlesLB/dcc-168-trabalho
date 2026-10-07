package br.ufjf.dcc168.tictactoe.io;

/**
 * Fonte de entrada do jogo.
 *
 * <p>Abstrai a origem dos dados (teclado, arquivo, roteiro de teste) para que a lógica de interação
 * possa ser testada sem depender do System.in.
 */
public interface InputReader {

    /**
     * Lê a próxima linha de texto.
     *
     * @return a linha lida, sem a quebra de linha final
     * @throws InputExhaustedException se não houver mais entrada disponível
     */
    String readLine();
}
