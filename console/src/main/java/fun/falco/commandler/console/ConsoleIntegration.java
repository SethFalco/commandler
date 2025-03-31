/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
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

package fun.falco.commandler.console;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fun.falco.commandler.api.ActionListener;
import fun.falco.commandler.api.Integration;
import fun.falco.commandler.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a minimal integration designed to handle console input.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
public class ConsoleIntegration implements Integration<String, String> {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleIntegration.class);

    private static final AtomicInteger i = new AtomicInteger();

    private final ActionListener listener;

    /**
     * Creates a scanner and prompts for input on a new thread.
     */
    @Inject
    public ConsoleIntegration(ActionListener listener) {
        logger.debug("Constructed instance of ConsoleIntegration.");
        this.listener = listener;
    }

    @Override
    public void init() {
        Executors.newSingleThreadExecutor().submit(() -> {
            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

            logger.info("Started listening to events in the console.");
            String nextLine;

            try {
                while ((nextLine = scanner.nextLine()) != null) {
                    String response;

                    try {
                        logger.debug("Receive `{}` from {}.", nextLine, this.getClass());
                        response = listener.onAction(this, nextLine, nextLine, nextLine);
                    } catch (Exception ex) {
                        logger.error("Failed to process Console event.", ex);
                        continue;
                    }

                    if (response != null) {
                        send(null, response);
                    } else {
                        logger.info("A message was received in console, however it warranted no response.");
                    }
                }
            } catch (Exception ex) {
                logger.error("Unable to read from the commandline. Make sure there is a connected terminal.", ex);
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

    /**
     * @param event Is ignored.
     * @param message Message to send.
     */
    @Override
    public void send(ActionEvent<String, String> event, String message) {
        logger.info("Response to command was: {}", message);
    }
}
