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

package org.elypia.commandler.dispatchers.match;

import org.elypia.commandler.CommandlerExtension;
import org.elypia.commandler.annotation.stereotypes.MessageDispatcher;
import org.elypia.commandler.api.*;
import org.elypia.commandler.event.*;
import org.elypia.commandler.exceptions.misuse.ParamCountMismatchException;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.regex.*;

/**
 * <p>
 *    The {@link MatchDispatcher} is a {@link Dispatcher} implementation
 *    which uses regular expression to match commands and matching groups
 *    as command parameters.
 * </p>
 *
 * <p>For example the following {@link Pattern}:</p>
 * <p><strong><code>(?i)\b([\d,.]+)\h*(KG|LBS?)\b</code></strong></p>
 *
 * May be matched by any of the following messages:
 * <ul>
 *     <li>I weigh 103KG!</li>
 *     <li>Yeah the laptop on the last LinusTechTips video was like 4.5 Lbs.</li>
 * </ul>
 *
 * In each case the parameters would be the capture groups, in this case
 * capturing a numeric value, then the units next to it.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@MessageDispatcher
public class MatchDispatcher implements Dispatcher {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(MatchDispatcher.class);

    /**
     * Rather than compiling patterns each time they're required,
     * we can store them in a map as we compile them and get them back,
     * when required again.
     */
    private static final Map<String, Pattern> PATTERNS = new HashMap<>();

    private final CommandlerExtension commmanderExtension;

    @Inject
    public MatchDispatcher(CommandlerExtension commmanderExtension) {
        this.commmanderExtension = Objects.requireNonNull(commmanderExtension);
    }

    /**
     * Any message could match a potential regular expression.
     * As a result all messages are valid Match commands.
     *
     * @param request The action request made by the {@link Integration}.
     * @param <S> The type of source even thtis {@link Integration} is for.
     * @param <M> The type of message this {@link Integration} sends and received.
     * @return If this is a valid command or not.
     */
    @Override
    public <S, M> boolean isValid(Request<S, M> request) {
        return true;
    }

    @Override
    public <S, M> ActionEvent<S, M> parse(Request<S, M> request) {
        MetaController selectedMetaController = null;
        MetaCommand selectedMetaCommand = null;
        List<List<String>> parameters = null;

        for (MetaController metaController : commmanderExtension.getMetaControllers()) {
            for (MetaCommand metaCommand : metaController.getMetaCommands()) {
                MetaProperty patternProperty = metaCommand.getProperty(this.getClass(), "pattern");

                if (patternProperty == null)
                    continue;

                String patternString = patternProperty.getValue();
                final Pattern pattern;

                if (!PATTERNS.containsKey(patternString)) {
                    pattern = Pattern.compile(patternString);
                    PATTERNS.put(patternString, pattern);
                } else {
                    pattern = PATTERNS.get(patternString);
                }

                Matcher matcher = pattern.matcher(request.getContent());

                if (!matcher.find())
                    return null;

                selectedMetaController = metaController;
                selectedMetaCommand = metaCommand;
                parameters = new ArrayList<>();

                for (int i = 0; i < matcher.groupCount(); i++) {
                    String group = matcher.group(i + 1);
                    parameters.add(List.of(group));
                }

                break;
            }
        }

        if (selectedMetaController == null)
            return null;

        Serializable id = request.getIntegration().getActionId(request.getSource());

        if (id == null)
            throw new IllegalStateException("All user interactions must be associated with a serializable ID.");

        Action action = new Action(id, request.getContent(), selectedMetaController.getControllerType(), selectedMetaCommand.getMethod().getName(), parameters);
        ActionEvent<S, M> e = new ActionEvent<>(request, action, selectedMetaController, selectedMetaCommand);

        if (!selectedMetaCommand.isValidParamCount(parameters.size()))
            throw new ParamCountMismatchException(e);

        return e;
    }
}
