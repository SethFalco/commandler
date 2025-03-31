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

package org.elypia.commandler.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Generic utilities for which can be used in chat.
 * Most utilities provided are required by Commandler.
 *
 * @author seth@falco.fun (Seth Falco)
 */
public final class ChatUtils {

    /** Many chat services use two regional indicators to represent a country. */
    private static final Map<String, String> regionalIndicators = Map.ofEntries(
        Map.entry("A", "🇦"),
        Map.entry("B", "🇧"),
        Map.entry("C", "🇨"),
        Map.entry("D", "🇩"),
        Map.entry("E", "🇪"),
        Map.entry("F", "🇫"),
        Map.entry("G", "🇬"),
        Map.entry("H", "🇭"),
        Map.entry("I", "🇮"),
        Map.entry("J", "🇯"),
        Map.entry("K", "🇰"),
        Map.entry("L", "🇱"),
        Map.entry("M", "🇲"),
        Map.entry("N", "🇳"),
        Map.entry("O", "🇴"),
        Map.entry("P", "🇵"),
        Map.entry("Q", "🇶"),
        Map.entry("R", "🇷"),
        Map.entry("S", "🇸"),
        Map.entry("T", "🇹"),
        Map.entry("U", "🇺"),
        Map.entry("V", "🇻"),
        Map.entry("W", "🇼"),
        Map.entry("X", "🇽"),
        Map.entry("Y", "🇾"),
        Map.entry("Z", "🇿")
    );

    private ChatUtils() {
        // Do nothing
    }

    /**
     * Replace all upper case characters with regional indicators
     * characters instead.
     *
     * @param input Source string to replace from.
     * @return New string with all upper case characters replaced.
     */
    public static String replaceWithIndicators(String input) {
        for (Map.Entry<String, String> entry : regionalIndicators.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }

        return input;
    }

    public static String replaceFromIndicators(String input) {
        for (Map.Entry<String, String> entry : regionalIndicators.entrySet()) {
            input = input.replace(entry.getValue(), entry.getKey());
        }

        return input;
    }

    /**
     * @param body String body to truncate.
     * @param maxLength Maximum length the result can be.
     * @return String itself, or a truncated version of the string.
     */
    public static String truncate(String body, int maxLength) {
        return truncateAndAppend(body, maxLength, null);
    }

    /**
     * @param body String body to truncate.
     * @param maxLength Maximum length the result can be.
     * @param append What to append to the end of the string if truncated.
     * @return String itself, or a truncated version of the string.
     */
    public static String truncateAndAppend(String body, int maxLength, String append) {
        if (append != null) {
            maxLength -= append.length();
        }

        String truncated = StringUtils.truncate(body, maxLength);

        if (append != null && truncated.length() != body.length()) {
            truncated += append;
        }

        return truncated;
    }
}
