/*
 * Copyright 2019-2020 Elypia CIC
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

package org.elypia.commandler.event;

import org.elypia.commandler.api.Integration;

import java.util.*;

/**
 * A {@link Request} represents each and every event
 * that comes into Commandler that could be a possible command.
 *
 * This has no knowledge of what command the user wants to perform
 * but only tracks the request for a potential interaction with the application.
 *
 * Regardless of which {@link Integration} provided the message,
 * it will first be mapped to this.
 *
 * @author seth@elypia.org
 */
public class Request<S, M> {

    /** The {@link Integration} that received this message. */
    private final Integration<S, M> integration;

    /** The source event object that was delivered from the {@link Integration}. */
    private final S source;

    /** The source message object that was delivered from the {@link Integration}. */
    private final M message;

    /** The content of the message, or an equivilent content if the event is not a string. */
    private final String content;

    /** Headers that define how this request is processed. */
    private final Map<String, String> headers;

    public Request(Integration<S, M> integration, S source, M message, String content) {
        this.integration = integration;
        this.source = source;
        this.message = message;
        this.content = content;
        this.headers = new HashMap<>();
    }

    public Integration<S, M> getIntegration() {
        return integration;
    }

    public S getSource() {
        return source;
    }

    public M getMessage() {
        return message;
    }

    public String getContent() {
        return content;
    }

    /**
     * @param key The name of the header.
     * @param value The value of the header, seperate with semi-colons (;) if it's a list.
     * @throws IllegalStateException if you try to set a key which has already been set.
     */
    public void setHeader(String key, String value) {
        if (headers.containsKey(key))
            throw new IllegalStateException("Headers in an event can't be overridden.");

        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");

        headers.forEach((key, value) -> {
            if (value == null)
                return;

            joiner.add(key + "=" + "\"" + value + "\"");
        });

        return this.getClass() + ":" + joiner.toString();
    }
}
