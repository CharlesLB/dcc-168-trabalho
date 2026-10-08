package br.ufjf.dcc168.tictactoe.report;

import br.ufjf.dcc168.tictactoe.specification.EquivalenceClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Cada método @Test anotado vira uma linha da Tabela 2. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestCase {

    String id();

    /** Coluna "Condições de Entrada". */
    String input();

    /** Coluna "Saída Esperada". */
    String expected();

    /** Coluna "Classes Eq. Exercitadas" (TestSet-Func). */
    EquivalenceClass[] classes() default {};

    /**
     * Requisito coberto: estrutural no TestSet-Estr (ex.: "Position.toString, linha 35") ou o
     * mutante alvo nos casos de mutação. Aparece na Tabela 2 no lugar das classes de equivalência.
     */
    String requirement() default "";
}
