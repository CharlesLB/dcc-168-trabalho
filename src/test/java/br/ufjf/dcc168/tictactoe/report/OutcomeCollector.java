package br.ufjf.dcc168.tictactoe.report;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.HashMap;
import java.util.Map;

public class OutcomeCollector extends RunListener {

    private final Map<String, TestOutcome> outcomesByTest = new HashMap<>();

    /** Resultado do teste; NOT_RUN se ele foi ignorado ou não chegou a rodar. */
    public TestOutcome outcomeOf(Class<?> testClass, String methodName) {
        TestOutcome outcome = outcomesByTest.get(key(testClass.getName(), methodName));
        return outcome == null ? TestOutcome.NOT_RUN : outcome;
    }

    @Override
    public void testStarted(Description description) {
        record(description, TestOutcome.PASSED);
    }

    @Override
    public void testFailure(Failure failure) {
        record(failure.getDescription(), TestOutcome.failed(describe(failure)));
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        record(failure.getDescription(), TestOutcome.NOT_RUN);
    }

    private void record(Description description, TestOutcome outcome) {
        outcomesByTest.put(key(description.getClassName(), description.getMethodName()), outcome);
    }

    // Sem mensagem (ex.: exceção inesperada sem texto), mostra pelo menos o tipo da exceção.
    private static String describe(Failure failure) {
        Throwable cause = failure.getException();
        String message = cause.getMessage();
        String type = cause.getClass().getSimpleName();
        return message == null ? type : type + ": " + message;
    }

    private static String key(String className, String methodName) {
        return className + "#" + methodName;
    }
}
