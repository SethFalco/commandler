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

package org.elypia.commandler.managers;

import org.elypia.commandler.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.config.ControllerConfig;
import org.elypia.commandler.metadata.MetaController;
import org.elypia.commandler.testing.*;
import org.slf4j.*;

import javax.inject.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class TestManager {

    private static final Logger logger = LoggerFactory.getLogger(TestManager.class);

    /**
     * The time this reporter was created and started
     * testing modules in Commandler.
     */
    private final Instant started;

    /**
     * Mapping of modules / command testReports to the respective
     * report instances to view report data.
     */
    private final Map<Class<? extends Controller>, ModuleReport> testReports;

    private final ScheduledExecutorService executor;

    @Inject
    public TestManager(CdiInjector cdiInjector, AppContext appContext) {
        started = Instant.now();
        testReports = new HashMap<>();
        executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            for (MetaController data : cdiInjector.getInstance(ControllerConfig.class).getControllers()) {
                Controller controller = cdiInjector.getInstance(data.getHandlerType());

                if (controller == null) {
                    logger.debug("Registered handler is not initalised, testing was skipped.");
                    continue;
                }

                Class<? extends Controller> type = controller.getClass();
                Report report = test(controller);

                if (!testReports.containsKey(type))
                    testReports.put(type, new ModuleReport());

                ModuleReport moduleReport = testReports.get(controller.getClass());
                moduleReport.add(report);
            }
        }, 0, 30000, TimeUnit.MINUTES);
    }

    public Report test(Controller controller) {
        long start = System.currentTimeMillis();

        try {
            boolean passed = controller.test();

            if (!passed)
                logger.warn("Test for module {} failed.", controller.getClass().getName());

            return new Report(passed, System.currentTimeMillis() - start);
        } catch (Exception ex) {
            logger.error("Test for module " + controller.getClass().getName() + " had an exception.", ex);
            return new Report(false, System.currentTimeMillis() - start, ex);
        }
    }

    public boolean isFailing(Controller controller) {
        Class<? extends Controller> type = controller.getClass();

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
