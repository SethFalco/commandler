/*
 * Copyright 2019-2019 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.testing;

import java.time.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
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
