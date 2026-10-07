package br.ufjf.dcc168.tictactoe.causeeffect;

import static br.ufjf.dcc168.tictactoe.causeeffect.CauseEffectGraph.and;
import static br.ufjf.dcc168.tictactoe.causeeffect.CauseEffectGraph.not;
import static br.ufjf.dcc168.tictactoe.causeeffect.CauseEffectGraph.or;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class DecisionTableTest {

    @Test
    public void andGraph_producesOneRulePerDistinctOutcome() {
        CauseEffectGraph graph = new CauseEffectGraph("E");
        Node first = graph.cause("C1", "primeira");
        Node second = graph.cause("C2", "segunda");
        Node accepted = graph.effect("E1", "aceita", and(first, second));
        Node rejected = graph.effect("E2", "rejeita", not(accepted));

        List<DecisionRule> rules = DecisionTable.from(graph).getRules();

        // E1: V V | E2: F – e V F (ou equivalente), 3 regras no total
        assertEquals(3, rules.size());
        assertRule(rules.get(0), "VV", accepted);
        assertTrue(rules.get(1).triggers(rejected));
        assertTrue(rules.get(2).triggers(rejected));
    }

    @Test
    public void causeThatDoesNotMatter_becomesAny() {
        CauseEffectGraph graph = new CauseEffectGraph("OU");
        Node first = graph.cause("C1", "primeira");
        Node second = graph.cause("C2", "segunda");
        Node effect = graph.effect("E1", "dispara", or(first, second));

        List<DecisionRule> rules = DecisionTable.from(graph).getRules();

        // C1 = V já basta: C2 não importa
        assertRule(rules.get(0), "V–", effect);
    }

    private static void assertRule(DecisionRule rule, String expectedCauses, Node expectedEffect) {
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < expectedCauses.length(); i++) {
            actual.append(rule.valueOfCause(i).getSymbol());
        }
        assertEquals(expectedCauses, actual.toString());
        assertTrue(rule.triggers(expectedEffect));
    }
}
