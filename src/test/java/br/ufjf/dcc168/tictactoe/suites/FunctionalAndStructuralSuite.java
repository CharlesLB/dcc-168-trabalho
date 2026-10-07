package br.ufjf.dcc168.tictactoe.suites;

import br.ufjf.dcc168.tictactoe.structural.GameStructuralTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** TestSet-Func + TestSet-Estr: usado na Parte II-B e na Parte III-A. */
@RunWith(Suite.class)
@SuiteClasses({FunctionalSuite.class, GameStructuralTest.class})
public class FunctionalAndStructuralSuite {}
