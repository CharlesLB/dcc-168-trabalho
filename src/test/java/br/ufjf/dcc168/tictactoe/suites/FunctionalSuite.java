package br.ufjf.dcc168.tictactoe.suites;

import br.ufjf.dcc168.tictactoe.functional.ConsoleFunctionalTest;
import br.ufjf.dcc168.tictactoe.functional.GameFunctionalTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** TestSet-Func isolado (CT01–CT21). */
@RunWith(Suite.class)
@SuiteClasses({GameFunctionalTest.class, ConsoleFunctionalTest.class})
public class FunctionalSuite {}
