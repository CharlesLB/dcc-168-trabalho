package br.ufjf.dcc168.tictactoe.specification;

/**
 * Condições de entrada da especificação (coluna "Condição de Entrada" da Tabela 1).
 *
 * <p>EXEMPLOS: o grupo deve revisar e completar esta lista na Parte I.
 */
public enum InputCondition {
    ROW("Linha da jogada"),
    COLUMN("Coluna da jogada"),
    TARGET_CELL("Célula escolhida"),
    GAME_STATE("Estado da partida");

    private final String description;

    InputCondition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
