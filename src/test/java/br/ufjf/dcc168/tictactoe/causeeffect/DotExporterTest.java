package br.ufjf.dcc168.tictactoe.causeeffect;

import static br.ufjf.dcc168.tictactoe.causeeffect.CauseEffectGraph.and;
import static br.ufjf.dcc168.tictactoe.causeeffect.CauseEffectGraph.not;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DotExporterTest {

    private final String dot = buildSampleDot();

    @Test
    public void andGate_showsSymbolInsideTargetNode() {
        assertTrue(dot.contains("E1 [shape=box, style=\"rounded,bold\", label=\"E1 ∧\\naceita\"]"));
    }

    @Test
    public void negatedEdge_isDashedWithTilde() {
        assertTrue(dot.contains("C2 -> E1 [style=dashed, headlabel=\"~\""));
    }

    @Test
    public void causes_areChainedInvisiblyToKeepTheirOrder() {
        assertTrue(dot.contains("C1 -> C2 [style=invis];"));
    }

    @Test
    public void quotesInDescription_areEscaped() {
        assertTrue(dot.contains("label=\"C1\\nlinha \\\"válida\\\"\""));
    }

    private static String buildSampleDot() {
        CauseEffectGraph graph = new CauseEffectGraph("Exemplo");
        Node first = graph.cause("C1", "linha \"válida\"");
        Node second = graph.cause("C2", "célula ocupada");
        graph.effect("E1", "aceita", and(first, not(second)));
        return DotExporter.toDot(graph);
    }
}
