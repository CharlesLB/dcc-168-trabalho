package br.ufjf.dcc168.tictactoe.causeeffect;

import java.util.Map;

/**
 * Nó do grafo de causa-efeito.
 *
 * <ul>
 *   <li>Causa (C1, C2...): condição de entrada, verdadeira ou falsa.
 *   <li>Intermediário (N1, N2...): combina causas para simplificar o desenho.
 *   <li>Efeito (E1, E2...): saída ou ação do programa.
 * </ul>
 */
public final class Node implements Operand {

    public enum Kind {
        CAUSE,
        INTERMEDIATE,
        EFFECT
    }

    private final String id;
    private final String description;
    private final Kind kind;
    private final Gate gate;

    Node(String id, String description, Kind kind, Gate gate) {
        this.id = id;
        this.description = description;
        this.kind = kind;
        this.gate = gate;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Kind getKind() {
        return kind;
    }

    /** Porta que alimenta este nó; null para causas. */
    public Gate getGate() {
        return gate;
    }

    /** Calcula o valor do nó a partir dos valores atribuídos às causas. */
    boolean evaluate(Map<Node, Boolean> causeValues) {
        if (kind == Kind.CAUSE) {
            return causeValues.get(this);
        }
        return gate.evaluate(causeValues);
    }

    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public boolean isNegated() {
        return false;
    }

    @Override
    public String toString() {
        return id;
    }
}
