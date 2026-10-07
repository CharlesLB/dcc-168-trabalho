package br.ufjf.dcc168.tictactoe.report;

import br.ufjf.dcc168.tictactoe.specification.EquivalenceClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Descreve um caso de teste para a Tabela 2. Cada método @Test anotado vira uma linha.
 *
 * <pre>
 * &#64;TestCase(
 *         id = "CT07",
 *         input = "&lt;X: (0,0), O: (0,0)&gt;",
 *         expected = "InvalidMoveException (CELL_OCCUPIED)",
 *         classes = {V1, V2, I5, V4})
 * &#64;Test
 * public void ct07_moveOnOccupiedCell_throwsInvalidMove() { ... }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestCase {

    /** Identificador único, ex.: "CT07". */
    String id();

    /** Coluna "Condições de Entrada". */
    String input();

    /** Coluna "Saída Esperada". */
    String expected();

    /** Coluna "Classes Eq. Exercitadas" (TestSet-Func). */
    EquivalenceClass[] classes() default {};

    /**
     * Requisito coberto: estrutural no TestSet-Estr (ex.: "Position.toString, linha 42") ou o
     * mutante alvo nos casos de mutação. Aparece na Tabela 2 no lugar das classes de equivalência.
     */
    String requirement() default "";
}
