package br.ufjf.dcc168.tictactoe.domain.report;

import br.ufjf.dcc168.tictactoe.domain.specification.EquivalenceClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Cada método @Test anotado vira uma linha da tabela de casos de teste do seu conjunto. */
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
     * Coluna "Requisito Estrutural Coberto" (TestSet-Estr): o nó, ramo ou par def-uso que o
     * TestSet-Func não cobre e que o caso passa a cobrir.
     */
    String requirement() default "";
}
