package br.ufjf.dcc168.tictactoe.suites;

import br.ufjf.dcc168.tictactoe.mutation.GameMutationTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** Conjunto final (Func + Estr + Mutação): usado na Parte III-B. */
@RunWith(Suite.class)
@SuiteClasses({FunctionalAndStructuralSuite.class, GameMutationTest.class})
public class AllTestsSuite {}
