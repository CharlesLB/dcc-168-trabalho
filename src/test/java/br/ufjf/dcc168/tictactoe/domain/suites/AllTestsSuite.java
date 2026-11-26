package br.ufjf.dcc168.tictactoe.domain.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** TestSet-Func + TestSet-Estr: o conjunto medido na Parte II-B. */
@RunWith(Suite.class)
@SuiteClasses({FunctionalSuite.class, StructuralSuite.class})
public class AllTestsSuite {}
