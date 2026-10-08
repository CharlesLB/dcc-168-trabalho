package br.ufjf.dcc168.tictactoe.report;

public final class TestOutcome {

    public enum Status {
        PASSED,
        FAILED,
        NOT_RUN
    }

    public static final TestOutcome PASSED = new TestOutcome(Status.PASSED, "");
    public static final TestOutcome NOT_RUN = new TestOutcome(Status.NOT_RUN, "");

    private final Status status;
    private final String failureMessage;

    private TestOutcome(Status status, String failureMessage) {
        this.status = status;
        this.failureMessage = failureMessage;
    }

    public static TestOutcome failed(String failureMessage) {
        return new TestOutcome(Status.FAILED, failureMessage);
    }

    public Status getStatus() {
        return status;
    }

    public String getFailureMessage() {
        return failureMessage;
    }
}
