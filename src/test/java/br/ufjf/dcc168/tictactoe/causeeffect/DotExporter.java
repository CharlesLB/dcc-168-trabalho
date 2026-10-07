package br.ufjf.dcc168.tictactoe.causeeffect;

import br.ufjf.dcc168.tictactoe.causeeffect.Gate.Operator;
import br.ufjf.dcc168.tictactoe.causeeffect.Node.Kind;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Escreve o grafo no formato DOT do Graphviz.
 *
 * <p>Convenções do desenho (notação de Myers):
 *
 * <ul>
 *   <li>causas à esquerda, efeitos à direita, nós intermediários no meio;
 *   <li>∧ / ∨ dentro do nó indicam a porta E / OU que combina as entradas;
 *   <li>aresta tracejada com "~" perto da ponta da seta indica negação.
 * </ul>
 */
public final class DotExporter {

    private static final String NEGATION_LABEL =
            "headlabel=\"~\", labeldistance=2.2, labelangle=25, labelfontsize=16";

    private DotExporter() {}

    public static String toDot(CauseEffectGraph graph) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph CauseEffect {\n");
        dot.append("    label=").append(quote(graph.getTitle())).append(";\n");
        dot.append("    labelloc=t;\n");
        dot.append("    rankdir=LR;\n");
        dot.append("    nodesep=0.4;\n");
        dot.append("    ranksep=0.9;\n");
        dot.append("    fontname=\"Helvetica\";\n");
        dot.append("    node [fontname=\"Helvetica\", fontsize=11];\n");
        dot.append("    edge [arrowsize=0.7];\n\n");

        appendSameRank(dot, "Causas", graph.getCauses());
        appendNodes(dot, "Nós intermediários", graph.getIntermediates());
        appendSameRank(dot, "Efeitos", graph.getEffects());

        dot.append("    // Arestas\n");
        for (Node node : graph.getNodes()) {
            appendIncomingEdges(dot, node);
        }
        dot.append("}\n");
        return dot.toString();
    }

    private static void appendSameRank(StringBuilder dot, String comment, List<Node> nodes) {
        dot.append("    // ").append(comment).append('\n');
        dot.append("    { rank=same;\n");
        for (Node node : nodes) {
            dot.append("    ").append(nodeDeclaration(node));
        }
        appendInvisibleOrderChain(dot, nodes);
        dot.append("    }\n\n");
    }

    // Sem isso o Graphviz pode desenhar C6 em cima e C1 embaixo; a corrente invisível fixa a ordem.
    private static void appendInvisibleOrderChain(StringBuilder dot, List<Node> nodes) {
        if (nodes.size() < 2) {
            return;
        }
        String chain = nodes.stream().map(Node::getId).collect(Collectors.joining(" -> "));
        dot.append("        ").append(chain).append(" [style=invis];\n");
    }

    private static void appendNodes(StringBuilder dot, String comment, List<Node> nodes) {
        dot.append("    // ").append(comment).append('\n');
        for (Node node : nodes) {
            dot.append(nodeDeclaration(node));
        }
        dot.append('\n');
    }

    private static String nodeDeclaration(Node node) {
        return "    "
                + node.getId()
                + " ["
                + shapeOf(node)
                + ", label="
                + quote(labelOf(node))
                + "];\n";
    }

    private static String shapeOf(Node node) {
        switch (node.getKind()) {
            case CAUSE:
                return "shape=box, style=rounded";
            case INTERMEDIATE:
                return "shape=circle, width=0.6, fixedsize=true";
            default:
                return "shape=box, style=\"rounded,bold\"";
        }
    }

    // Ex.: "C1\nLinha entre 0 e 2", "N1\n∧", "E4 ∧\nJogador vence"
    private static String labelOf(Node node) {
        String gateSymbol = gateSymbolOf(node);
        if (node.getKind() == Kind.INTERMEDIATE) {
            return gateSymbol.isEmpty() ? node.getId() : node.getId() + "\n" + gateSymbol;
        }
        String header = gateSymbol.isEmpty() ? node.getId() : node.getId() + " " + gateSymbol;
        return header + "\n" + node.getDescription();
    }

    private static String gateSymbolOf(Node node) {
        Gate gate = node.getGate();
        if (gate == null || gate.getOperator() == Operator.IDENTITY) {
            return "";
        }
        return gate.getOperator().getSymbol();
    }

    private static void appendIncomingEdges(StringBuilder dot, Node target) {
        if (target.getGate() == null) {
            return;
        }
        for (Operand operand : target.getGate().getOperands()) {
            dot.append("    ")
                    .append(operand.getNode().getId())
                    .append(" -> ")
                    .append(target.getId());
            if (operand.isNegated()) {
                // "~" junto da ponta da seta: fica claro qual entrada está negada
                dot.append(" [style=dashed, " + NEGATION_LABEL + "]");
            }
            dot.append(";\n");
        }
    }

    private static String quote(String text) {
        String escaped = text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
        return "\"" + escaped + "\"";
    }
}
