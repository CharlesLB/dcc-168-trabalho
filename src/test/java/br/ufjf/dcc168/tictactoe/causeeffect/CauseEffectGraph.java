package br.ufjf.dcc168.tictactoe.causeeffect;

import br.ufjf.dcc168.tictactoe.causeeffect.Node.Kind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Grafo de causa-efeito montado em código.
 *
 * <pre>
 * CauseEffectGraph graph = new CauseEffectGraph("Jogada");
 * Node rowInRange = graph.cause("C1", "Linha entre 0 e 2");
 * Node columnInRange = graph.cause("C2", "Coluna entre 0 e 2");
 * Node validPosition = graph.intermediate("N1", and(rowInRange, columnInRange));
 * graph.effect("E1", "Rejeita a jogada", not(validPosition));
 * </pre>
 */
public final class CauseEffectGraph {

    private final String title;
    private final List<Node> nodes = new ArrayList<>();

    public CauseEffectGraph(String title) {
        this.title = title;
    }

    // ------------------------------------------------------------ construção

    public Node cause(String id, String description) {
        return add(new Node(id, description, Kind.CAUSE, null));
    }

    public Node intermediate(String id, Gate gate) {
        return add(new Node(id, "", Kind.INTERMEDIATE, gate));
    }

    public Node effect(String id, String description, Gate gate) {
        return add(new Node(id, description, Kind.EFFECT, gate));
    }

    /** Efeito com uma única entrada, ex.: {@code effect("E1", "...", not(validPosition))}. */
    public Node effect(String id, String description, Operand singleInput) {
        return effect(id, description, Gate.identity(singleInput));
    }

    public static Gate and(Operand... operands) {
        return Gate.and(operands);
    }

    public static Gate or(Operand... operands) {
        return Gate.or(operands);
    }

    public static Operand not(Node node) {
        return new Negation(node);
    }

    private Node add(Node node) {
        boolean idAlreadyUsed =
                nodes.stream().anyMatch(other -> other.getId().equals(node.getId()));
        if (idAlreadyUsed) {
            throw new IllegalArgumentException("Duplicate node id: " + node.getId());
        }
        nodes.add(node);
        return node;
    }

    // ------------------------------------------------------------ consulta

    public String getTitle() {
        return title;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<Node> getCauses() {
        return nodesOfKind(Kind.CAUSE);
    }

    public List<Node> getIntermediates() {
        return nodesOfKind(Kind.INTERMEDIATE);
    }

    public List<Node> getEffects() {
        return nodesOfKind(Kind.EFFECT);
    }

    private List<Node> nodesOfKind(Kind kind) {
        return nodes.stream().filter(node -> node.getKind() == kind).collect(Collectors.toList());
    }
}
