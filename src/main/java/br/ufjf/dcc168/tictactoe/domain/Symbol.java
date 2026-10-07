package br.ufjf.dcc168.tictactoe.domain;

/** Símbolo de um jogador. X sempre inicia a partida. */
public enum Symbol {
    X,
    O;

    /** Retorna o símbolo do adversário. */
    public Symbol opponent() {
        return this == X ? O : X;
    }
}
