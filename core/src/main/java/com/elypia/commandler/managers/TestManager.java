package com.elypia.commandler.managers;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.metadata.data.MetaModule;
import com.elypia.commandler.testing.*;
import org.slf4j.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

public class TestManager {

    private static final Logger logger = LoggerFactory.getLogger(TestManager.class);

    /**
     * The time this reporter was created and started
     * testing modules in Commandler.
     */
    private final Instant started;

    /** The Commandler instance to take modules from and test. */
    private final Commandler commandler;

    /**
     * Mapping of modules / command testReports to the respective
     * report instances to view report data.
     */
    private final Map<Class<? extends Handler>, ModuleReport> testReports;

    private final ScheduledExecutorService executor;

    public TestManager(Commandler commandler) {
        started = Instant.now();
        this.commandler = commandler;
        testReports = new HashMap<>();
        executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            for (MetaModule data : commandler.getContext()) {
                Handler handler = commandler.getInjector().getInstance(data.getHandlerType());

                if (handler == null) {
                    logger.debug("Registered handler is not initalised, testing was skipped.");
                    continue;
                }

                Class<? extends Handler> type = handler.getClass();
                Report report = test(handler);

                if (!testReports.containsKey(type))
                    testReports.put(type, new ModuleReport());

                ModuleReport moduleReport = testReports.get(handler.getClass());
                moduleReport.add(report);
            }
        }, 0, 30000, TimeUnit.MINUTES);
    }

    public Report test(Handler handler) {
        long start = System.currentTimeMillis();

        try {
            boolean passed = handler.test();

            if (!passed)
                logger.warn("Test for module {} failed.", handler.getClass().getName());

            return new Report(passed, System.currentTimeMillis() - start);
        } catch (Exception ex) {
            logger.error("Test for module " + handler.getClass().getName() + " had an exception.", ex);
            return new Report(false, System.currentTimeMillis() - start, ex);
        }
    }

    public boolean isFailing(Handler handler) {
        Class<? extends Handler> type = handler.getClass();

        if (!testReports.containsKey(type))
            return false;

        ModuleReport moduleReport = testReports.get(type);
        List<Report> reports = moduleReport.getReports();

        if (reports.isEmpty())
            return false;

        return !reports.get(reports.size() - 1).isPassed();
    }

    public Instant getStarted() {
        return started;
    }
}
