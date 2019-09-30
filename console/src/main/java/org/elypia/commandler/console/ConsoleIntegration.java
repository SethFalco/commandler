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

package org.elypia.commandler.console;

import org.elypia.commandler.Commandler;
import org.elypia.commandler.api.AbstractIntegration;
import org.slf4j.*;

import javax.inject.*;
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
    @Inject
    public ConsoleIntegration(Commandler commandler) {
        this.commandler = commandler;
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

                if (response != null)
                    System.out.println(response);
                else
                    logger.info("A message was recieved in console, however it warranted no response.");
            }
        });
    }

    @Override
    public Class<String> getMessageType() {
        return String.class;
    }

    @Override
    public Serializable getActionId(String source) {
        int id = i.addAndGet(1);
        logger.debug("ID for message `{}`: {}", source, id);
        return id;
    }
}
