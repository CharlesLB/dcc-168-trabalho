package br.ufjf.dcc168.tictactoe.causeeffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tabela de decisão derivada do grafo de causa-efeito.
 *
 * <p>Como é montada:
 *
 * <ol>
 *   <li>avalia o grafo para todas as combinações de V/F das causas;
 *   <li>junta regras que disparam os mesmos efeitos e diferem em uma única causa, marcando essa
 *       causa como "–" (tanto faz), até não haver mais o que juntar.
 * </ol>
 *
 * <p>O resultado é uma regra por coluna; cada regra vira pelo menos um caso de teste.
 */
public final class DecisionTable {

    // 2^12 = 4096 combinações; acima disso o grafo deveria ser dividido em partes menores.
    private static final int MAX_CAUSES = 12;

    private final List<Node> causes;
    private final List<Node> effects;
    private final List<DecisionRule> rules;

    private DecisionTable(List<Node> causes, List<Node> effects, List<DecisionRule> rules) {
        this.causes = causes;
        this.effects = effects;
        this.rules = rules;
    }

    public static DecisionTable from(CauseEffectGraph graph) {
        List<Node> causes = graph.getCauses();
        List<Node> effects = graph.getEffects();
        if (causes.size() > MAX_CAUSES) {
            throw new IllegalArgumentException("Too many causes: " + causes.size());
        }

        List<DecisionRule> rules = allCombinations(causes, effects);
        mergeWhilePossible(rules);
        sortByFirstEffect(rules, effects);
        return new DecisionTable(causes, effects, rules);
    }

    public List<Node> getCauses() {
        return causes;
    }

    public List<Node> getEffects() {
        return effects;
    }

    public List<DecisionRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    private static List<DecisionRule> allCombinations(List<Node> causes, List<Node> effects) {
        List<DecisionRule> rules = new ArrayList<>();
        int combinationCount = 1 << causes.size();

        for (int combination = 0; combination < combinationCount; combination++) {
            Map<Node, Boolean> causeValues = new HashMap<>();
            CauseValue[] values = new CauseValue[causes.size()];

            for (int i = 0; i < causes.size(); i++) {
                // A primeira causa é o bit mais significativo: as regras começam com C1 = V.
                boolean isTrue = (combination & (1 << (causes.size() - 1 - i))) == 0;
                causeValues.put(causes.get(i), isTrue);
                values[i] = isTrue ? CauseValue.TRUE : CauseValue.FALSE;
            }

            Set<Node> activeEffects = new LinkedHashSet<>();
            for (Node effect : effects) {
                if (effect.evaluate(causeValues)) {
                    activeEffects.add(effect);
                }
            }
            rules.add(new DecisionRule(values, activeEffects));
        }
        return rules;
    }

    private static void mergeWhilePossible(List<DecisionRule> rules) {
        boolean mergedSomething = true;
        while (mergedSomething) {
            mergedSomething = mergeFirstPair(rules);
        }
    }

    private static boolean mergeFirstPair(List<DecisionRule> rules) {
        for (int i = 0; i < rules.size(); i++) {
            for (int j = i + 1; j < rules.size(); j++) {
                DecisionRule first = rules.get(i);
                DecisionRule second = rules.get(j);
                if (first.canMergeWith(second)) {
                    DecisionRule merged = first.mergeWith(second);
                    rules.remove(j);
                    rules.remove(i);
                    if (!rules.contains(merged)) {
                        rules.add(i, merged);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    // Agrupa as regras pelo primeiro efeito que disparam, na ordem E1, E2, ...
    private static void sortByFirstEffect(List<DecisionRule> rules, List<Node> effects) {
        rules.sort(
                (first, second) ->
                        Integer.compare(
                                firstEffectIndex(first, effects),
                                firstEffectIndex(second, effects)));
    }

    private static int firstEffectIndex(DecisionRule rule, List<Node> effects) {
        for (int i = 0; i < effects.size(); i++) {
            if (rule.triggers(effects.get(i))) {
                return i;
            }
        }
        return effects.size();
    }
}
