package br.ufjf.dcc168.tictactoe.domain.suites;

import br.ufjf.dcc168.tictactoe.structural.ConsoleIoStructuralTest;
import br.ufjf.dcc168.tictactoe.structural.MainStructuralTest;
import br.ufjf.dcc168.tictactoe.structural.PositionStructuralTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** TestSet-Estr isolado (CE01 a CE05), os casos acrescentados na Parte II-B. */
@RunWith(Suite.class)
@SuiteClasses({
    PositionStructuralTest.class,
    ConsoleIoStructuralTest.class,
    MainStructuralTest.class
})
public class StructuralSuite {}
