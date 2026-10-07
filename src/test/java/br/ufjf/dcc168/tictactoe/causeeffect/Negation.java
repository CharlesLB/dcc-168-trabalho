package br.ufjf.dcc168.tictactoe.causeeffect;

/** Operando negado: o símbolo "~" sobre a aresta no grafo de causa-efeito. */
final class Negation implements Operand {

    private final Node node;

    Negation(Node node) {
        this.node = node;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public boolean isNegated() {
        return true;
    }
}
