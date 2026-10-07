package br.ufjf.dcc168.tictactoe.io;

/**
 * Destino de saída do jogo.
 *
 * <p>Abstrai o destino (console, arquivo, gravador de teste) para que as mensagens exibidas possam
 * ser verificadas nos testes.
 */
public interface OutputPrinter {

    /** Escreve o texto e quebra a linha. */
    void printLine(String text);

    /** Escreve uma linha vazia. */
    default void printEmptyLine() {
        printLine("");
    }
}
