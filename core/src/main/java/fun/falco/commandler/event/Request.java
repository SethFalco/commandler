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

package fun.falco.commandler.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import fun.falco.commandler.api.Integration;

/**
 * A {@link Request} represents each and every event that comes into Commandler
 * that could be a possible command.
 *
 * <p>This has no knowledge of what command the user wants to perform but only
 * tracks the request for a potential interaction with the application.</p>
 *
 * <p>Regardless of which {@link Integration} provided the message, it will
 * first be mapped to this.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
public class Request<S, M> {

    /** Integration that received this message. */
    private final Integration<S, M> integration;

    /**
     * The source event object that was delivered from the {@link Integration}.
     */
    private final S source;

    /**
     * The source message object that was delivered from the
     * {@link Integration}. Depending on the {@link Integration} this may be the
     * same as {@link #source}.
     */
    private final M message;

    /**
     * The content of the message, or an equivalent content if the event is not
     * a string.
     */
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
     * @param key Name of the header.
     * @param value
     *     Value of the header, separate with semi-colons (;) if it's a list.
     * @throws IllegalStateException
     *     If you try to set a key which has already been set.
     */
    public void setHeader(String key, String value) {
        if (headers.containsKey(key)) {
            throw new IllegalStateException("Headers in an event can't be overridden.");
        }

        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");

        headers.forEach((key, value) -> {
            if (value == null) {
                return;
            }

            joiner.add(key + "=" + "\"" + value + "\"");
        });

        return this.getClass() + ":" + joiner.toString();
    }
}
