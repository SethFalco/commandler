package com.elypia.commandler.testing;

import com.elypia.commandler.*;
import com.elypia.commandler.metadata.ModuleData;
import org.slf4j.*;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    /**
     * The time this reporter was created and started
     * testing modules in Commandler.
     */
    private final Instant started;

    /**
     * The Commandler instance to take modules from and test.
     */
    private final Commandler commandler;

    /**
     * Mapping of modules / command testReports to the respective
     * report instances to view report data.
     */
    private final Map<Class<? extends Handler>, ModuleReport> testReports;

    private final ScheduledExecutorService executor;

    private final List<Consumer<Report>> postActions;

    public TestRunner(Commandler commandler) {
        started = Instant.now();
        this.commandler = commandler;
        testReports = new HashMap<>();
        executor = Executors.newSingleThreadScheduledExecutor();
        postActions = new ArrayList<>();

        begin();
    }

    public void addPostAction(Consumer<Report> action) {
        postActions.add(action);
    }

    private void begin() {
        executor.scheduleAtFixedRate(() -> {
            for (ModuleData data : commandler.getContext()) {
                try {
                    Handler handler = data.getInstance(commandler);

                    if (handler == null) {
                        logger.debug("Registered handler is not initalised, testing was skipped.");
                        continue;
                    }

                    Class<? extends Handler> clazz = handler.getClass();
                    Report report = test(handler);

                    if (!testReports.containsKey(clazz))
                        testReports.put(clazz, new ModuleReport());

                    ModuleReport moduleReport = testReports.get(handler.getClass());
                    moduleReport.add(report);

                    for (Consumer<Report> action : postActions)
                        action.accept(report);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    logger.error("Failed to instantiate module.");
                }
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
        Class<? extends Handler> clazz = handler.getClass();

        if (!testReports.containsKey(clazz))
            return false;

        ModuleReport moduleReport = testReports.get(clazz);
        List<Report> reports = moduleReport.getReports();

        if (reports.isEmpty())
            return false;

        return !reports.get(reports.size() - 1).isPassed();
    }

    public Instant getStarted() {
        return started;
    }
}
