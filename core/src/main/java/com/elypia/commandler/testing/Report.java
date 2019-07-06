package com.elypia.commandler.testing;

import java.time.*;

public class Report {

    /** The time this report was logged. */
    private final Instant timestamp;

    /** If this test passed. */
    private final boolean passed;

    /** The duration this test went on for. */
    private final Duration duration;

    /** Only non-null if the test failed because an exception occured. */
    private final Exception ex;

    public Report(boolean passed, long millis) {
        this(passed, millis, null);
    }

    public Report(boolean passed, long millis, Exception ex) {
        timestamp = Instant.now();
        this.passed = passed;
        duration = Duration.ofMillis(millis);
        this.ex = ex;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public boolean isPassed() {
        return passed;
    }

    public Duration getDuration() {
        return duration;
    }

    public Exception getException() {
        return ex;
    }
}
