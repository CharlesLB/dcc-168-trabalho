package br.ufjf.dcc168.tictactoe.causeeffect;

/** Entrada de uma porta lógica: um nó, possivelmente negado (~). */
public interface Operand {

    Node getNode();

    boolean isNegated();
}
