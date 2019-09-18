package org.elypia.commandler.console;

import org.elypia.commandler.api.AbstractIntegration;
import org.slf4j.*;

import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a minimal integration designed to handle console input.
 *
 * @author seth@elypia.com (Syed Shah)
 */
@Singleton
public class ConsoleIntegration extends AbstractIntegration<String, String> {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleIntegration.class);

    private static final AtomicInteger i = new AtomicInteger();

    /**
     * Creates a scanner and prompts for input on a new thread.
     *
     */
    public ConsoleIntegration() {
        Scanner scanner = new Scanner(System.in);

        Executors.newSingleThreadExecutor().submit(() -> {
            String nextLine;

            while ((nextLine = scanner.nextLine()) != null) {
                String response;

                try {
                    response = process(nextLine, nextLine);
                } catch (Exception ex) {
                    logger.error("Failed to process Console event.", ex);
                    throw ex;
                }

                System.out.println(response);
            }
        });
    }

    @Override
    public Class<String> getMessageType() {
        return String.class;
    }

    @Override
    public Serializable getActionId(String source) {
        return i.addAndGet(1);
    }
}
