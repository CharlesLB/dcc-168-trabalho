package br.ufjf.dcc168.tictactoe.specification;

import static br.ufjf.dcc168.tictactoe.causeeffect.CauseEffectGraph.and;
import static br.ufjf.dcc168.tictactoe.causeeffect.CauseEffectGraph.not;

import br.ufjf.dcc168.tictactoe.causeeffect.CauseEffectGraph;
import br.ufjf.dcc168.tictactoe.causeeffect.Node;

/**
 * Grafo de causa-efeito de uma jogada do Jogo da Velha (Parte I).
 *
 * <p>Fonte única do grafo: o ReportGenerator gera a partir daqui o .dot, a imagem PNG e a tabela de
 * decisão. PRIMEIRA VERSÃO: o grupo deve revisar causas, efeitos e ligações.
 *
 * <p>A ordem das verificações segue as regras definidas pelo grupo: posição válida, depois partida
 * em andamento, depois célula vazia; vitória é verificada antes do empate.
 */
public final class CauseEffectSpecification {

    private CauseEffectSpecification() {}

    public static CauseEffectGraph build() {
        CauseEffectGraph graph = new CauseEffectGraph("Grafo de Causa-Efeito – Jogada");

        // Causas
        Node rowInRange = graph.cause("C1", "Linha entre 0 e 2");
        Node columnInRange = graph.cause("C2", "Coluna entre 0 e 2");
        Node gameInProgress = graph.cause("C3", "Partida em andamento");
        Node cellEmpty = graph.cause("C4", "Célula escolhida vazia");
        Node completesLine = graph.cause("C5", "Jogada completa linha,\ncoluna ou diagonal");
        Node fillsLastCell = graph.cause("C6", "Jogada preenche a\núltima célula vazia");

        // Nós intermediários
        Node validPosition = graph.intermediate("N1", and(rowInRange, columnInRange));
        Node moveAccepted = graph.intermediate("N2", and(validPosition, gameInProgress, cellEmpty));

        // Efeitos
        graph.effect("E1", "Rejeita: posição\nfora do tabuleiro", not(validPosition));
        graph.effect(
                "E2", "Rejeita: partida\njá encerrada", and(validPosition, not(gameInProgress)));
        graph.effect(
                "E3",
                "Rejeita: célula ocupada",
                and(validPosition, gameInProgress, not(cellEmpty)));
        graph.effect("E4", "Jogador da vez vence", and(moveAccepted, completesLine));
        graph.effect(
                "E5",
                "Partida termina\nempatada",
                and(moveAccepted, not(completesLine), fillsLastCell));
        graph.effect(
                "E6",
                "Vez passa ao\nadversário",
                and(moveAccepted, not(completesLine), not(fillsLastCell)));

        return graph;
    }
}
