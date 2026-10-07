package br.ufjf.dcc168.tictactoe.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class OutcomeCollectorTest {

    /** Classe de exemplo com um teste de cada tipo. Surefire não roda classes internas. */
    public static class SampleTests {

        @Test
        public void passes() {}

        @Test
        public void fails() {
            assertEquals("esperado", "obtido");
        }

        @Ignore
        @Test
        public void isIgnored() {}
    }

    private final OutcomeCollector collector = runSampleTests();

    @Test
    public void passingTest_isRecordedAsPassed() {
        assertEquals(TestOutcome.Status.PASSED, outcomeOf("passes").getStatus());
    }

    @Test
    public void failingTest_isRecordedAsFailedWithMessage() {
        TestOutcome outcome = outcomeOf("fails");

        assertEquals(TestOutcome.Status.FAILED, outcome.getStatus());
        assertTrue(outcome.getFailureMessage().startsWith("ComparisonFailure: expected:"));
    }

    @Test
    public void ignoredTest_isRecordedAsNotRun() {
        assertEquals(TestOutcome.Status.NOT_RUN, outcomeOf("isIgnored").getStatus());
    }

    private TestOutcome outcomeOf(String methodName) {
        return collector.outcomeOf(SampleTests.class, methodName);
    }

    private static OutcomeCollector runSampleTests() {
        OutcomeCollector collector = new OutcomeCollector();
        JUnitCore junit = new JUnitCore();
        junit.addListener(collector);
        junit.run(SampleTests.class);
        return collector;
    }
}
