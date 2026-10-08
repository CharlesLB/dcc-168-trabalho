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

    /** Coluna "Classes Eq. Exercitadas". */
    EquivalenceClass[] classes();
}
