package br.ufjf.dcc168.tictactoe.causeeffect;

import java.util.Arrays;
import java.util.Set;

/**
 * Uma coluna da tabela de decisão: valores das causas e efeitos disparados. Cada regra corresponde
 * a (pelo menos) um caso de teste.
 */
public final class DecisionRule {

    private final CauseValue[] causeValues;
    private final Set<Node> activeEffects;

    DecisionRule(CauseValue[] causeValues, Set<Node> activeEffects) {
        this.causeValues = causeValues;
        this.activeEffects = activeEffects;
    }

    public CauseValue valueOfCause(int causeIndex) {
        return causeValues[causeIndex];
    }

    public boolean triggers(Node effect) {
        return activeEffects.contains(effect);
    }

    Set<Node> getActiveEffects() {
        return activeEffects;
    }

    /**
     * Duas regras se juntam quando disparam os mesmos efeitos e diferem em uma única causa: essa
     * causa não importa, então vira "–".
     */
    boolean canMergeWith(DecisionRule other) {
        return activeEffects.equals(other.activeEffects) && indexOfOnlyDifference(other) >= 0;
    }

    DecisionRule mergeWith(DecisionRule other) {
        CauseValue[] merged = causeValues.clone();
        merged[indexOfOnlyDifference(other)] = CauseValue.ANY;
        return new DecisionRule(merged, activeEffects);
    }

    /** Posição da única causa diferente (V x F), ou -1 se não houver exatamente uma. */
    private int indexOfOnlyDifference(DecisionRule other) {
        int differenceIndex = -1;
        for (int i = 0; i < causeValues.length; i++) {
            if (causeValues[i] == other.causeValues[i]) {
                continue;
            }
            boolean oneSideIsAny =
                    causeValues[i] == CauseValue.ANY || other.causeValues[i] == CauseValue.ANY;
            if (oneSideIsAny || differenceIndex >= 0) {
                return -1;
            }
            differenceIndex = i;
        }
        return differenceIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DecisionRule)) {
            return false;
        }
        DecisionRule rule = (DecisionRule) other;
        return Arrays.equals(causeValues, rule.causeValues)
                && activeEffects.equals(rule.activeEffects);
    }

    @Override
    public int hashCode() {
        return 31 * Arrays.hashCode(causeValues) + activeEffects.hashCode();
    }
}
