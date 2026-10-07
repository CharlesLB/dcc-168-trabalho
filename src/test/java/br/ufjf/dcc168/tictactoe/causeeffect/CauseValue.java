package br.ufjf.dcc168.tictactoe.causeeffect;

/** Valor de uma causa em uma regra da tabela de decisão. */
public enum CauseValue {
    TRUE("V"),
    FALSE("F"),
    /** Tanto faz: o valor da causa não altera os efeitos desta regra. */
    ANY("–");

    private final String symbol;

    CauseValue(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
