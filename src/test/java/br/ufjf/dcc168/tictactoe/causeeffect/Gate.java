package br.ufjf.dcc168.tictactoe.causeeffect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Porta lógica que liga as entradas a um nó intermediário ou a um efeito. */
public final class Gate {

    public enum Operator {
        AND("∧"),
        OR("∨"),
        /** Uma única entrada, ligada direto ao nó (com ou sem negação). */
        IDENTITY("");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    private final Operator operator;
    private final List<Operand> operands;

    private Gate(Operator operator, Operand... operands) {
        this.operator = operator;
        this.operands = Collections.unmodifiableList(Arrays.asList(operands));
    }

    static Gate and(Operand... operands) {
        requireAtLeastTwo(operands);
        return new Gate(Operator.AND, operands);
    }

    static Gate or(Operand... operands) {
        requireAtLeastTwo(operands);
        return new Gate(Operator.OR, operands);
    }

    static Gate identity(Operand operand) {
        return new Gate(Operator.IDENTITY, operand);
    }

    public Operator getOperator() {
        return operator;
    }

    public List<Operand> getOperands() {
        return operands;
    }

    boolean evaluate(Map<Node, Boolean> causeValues) {
        if (operator == Operator.OR) {
            return operands.stream().anyMatch(operand -> valueOf(operand, causeValues));
        }
        // AND e IDENTITY: todas as entradas precisam ser verdadeiras
        return operands.stream().allMatch(operand -> valueOf(operand, causeValues));
    }

    private static boolean valueOf(Operand operand, Map<Node, Boolean> causeValues) {
        boolean value = operand.getNode().evaluate(causeValues);
        return operand.isNegated() ? !value : value;
    }

    private static void requireAtLeastTwo(Operand[] operands) {
        if (operands.length < 2) {
            throw new IllegalArgumentException("AND/OR gates need at least two operands");
        }
    }
}
