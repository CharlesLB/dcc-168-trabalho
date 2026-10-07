package br.ufjf.dcc168.tictactoe.specification;

/**
 * Condições de entrada da especificação (coluna "Condição de Entrada" da Tabela 1 da Parte I).
 *
 * <p>A ordem das constantes é a ordem das linhas da Tabela 1. As duas últimas são classes de saída:
 * particionam o domínio pelas saídas distintas que o programa deve produzir.
 */
public enum InputCondition {
    INPUT_FORMAT("Formato da entrada"),
    ROW("Linha (L)"),
    COLUMN("Coluna (C)"),
    TARGET_CELL("Célula escolhida"),
    GAME_STATE("Estado da partida"),
    MOVE_RESULT("Resultado da jogada válida"),
    WINNING_PLAYER("Jogador que completa a linha");

    private final String description;

    InputCondition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
