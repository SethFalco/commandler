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

package org.elypia.commandler.dispatchers;

import java.util.*;
import java.util.regex.*;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 4.0.1
 */
public class StandardDispatcherParameterParser {

    /**
     * This matches every argument in the commands,
     * any comma seperated <strong>args</strong> will be
     * split by {@link #itemsPattern} as a list.
     */
    private static final Pattern paramsPattern = Pattern.compile("(?:(?:\"(?:[^\"])*\"|[^\\s,]+)(?:\\s*,\\s*)?)+");

    /**
     * The item regex, this matches every item within a list of parameters.
     * This is for list parameters as a single parameter can contain multiple items.
     */
    private static final Pattern itemsPattern = Pattern.compile("(?<!\\\\)\"(?<quote>.*?)(?<!\\\\)\"|(?<word>[^\\s]+(?<!,))");

    /**
     * @param paramString The string of parameters provided by the ser.
     * @return A list of list of strings that represent all params and items.
     */
    public List<List<String>> parse(String paramString) {
        List<List<String>> params = new ArrayList<>();

        if (paramString.isBlank())
            return params;

        Matcher paramMatcher = paramsPattern.matcher(paramString);

        while (paramMatcher.find()) {
            String group = paramMatcher.group();
            List<String> parameterItems = parseItems(group);
            params.add(parameterItems);
        }

        return params;
    }

    /**
     * @param param A parameter, or list of items to parse.
     * @return The parameter split into it's individual items.
     */
    public List<String> parseItems(String param) {
        if (param == null || param.isBlank())
            throw new IllegalStateException("Can't have parameter with no items.");

        List<String> list = new ArrayList<>();
        Matcher splitMatcher = itemsPattern.matcher(param);

        while (splitMatcher.find()) {
            String quote = splitMatcher.group("quote");
            String toAdd = (quote != null) ? quote : splitMatcher.group("word");

            list.add(toAdd);
        }

        return list;
    }
}
