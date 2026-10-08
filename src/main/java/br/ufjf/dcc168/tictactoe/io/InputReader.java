package br.ufjf.dcc168.tictactoe.io;

public interface InputReader {

    /**
     * @return a linha lida, sem a quebra de linha final
     * @throws InputExhaustedException se não houver mais entrada disponível
     */
    String readLine();
}
