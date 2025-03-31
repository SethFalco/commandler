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

package fun.falco.commandler.dispatchers.match;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import fun.falco.commandler.CommandlerExtension;
import fun.falco.commandler.annotation.stereotypes.MessageDispatcher;
import fun.falco.commandler.api.Dispatcher;
import fun.falco.commandler.api.Integration;
import fun.falco.commandler.event.Action;
import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.event.Request;
import fun.falco.commandler.exceptions.misuse.ParamCountMismatchException;
import fun.falco.commandler.metadata.MetaCommand;
import fun.falco.commandler.metadata.MetaController;
import fun.falco.commandler.metadata.MetaProperty;

/**
 * The {@link MatchDispatcher} is a {@link Dispatcher} implementation which uses
 * regular expression to match commands and matching groups as command
 * parameters.
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
 * @author seth@falco.fun (Seth Falco)
 */
@MessageDispatcher
public class MatchDispatcher implements Dispatcher {

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
     * Any message could match a potential regular expression. As a result all
     * messages are valid Match commands.
     *
     * @param request Action request made by the {@link Integration}.
     * @param <S> Type of source event this {@link Integration} is for.
     * @param <M> Type of message this {@link Integration} sends and received.
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

                if (patternProperty == null) {
                    continue;
                }

                String patternString = patternProperty.getValue();
                final Pattern pattern;

                if (!PATTERNS.containsKey(patternString)) {
                    pattern = Pattern.compile(patternString);
                    PATTERNS.put(patternString, pattern);
                } else {
                    pattern = PATTERNS.get(patternString);
                }

                Matcher matcher = pattern.matcher(request.getContent());

                if (!matcher.find()) {
                    return null;
                }

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

        if (selectedMetaController == null) {
            return null;
        }

        Serializable id = request.getIntegration().getActionId(request.getSource());

        if (id == null) {
            throw new IllegalStateException("All user interactions must be associated with a serializable ID.");
        }

        Action action = new Action(id, request.getContent(), selectedMetaController.getControllerType(), selectedMetaCommand.getMethod().getName(), parameters);
        ActionEvent<S, M> e = new ActionEvent<>(request, action, selectedMetaController, selectedMetaCommand);

        if (!selectedMetaCommand.isValidParamCount(parameters.size())) {
            throw new ParamCountMismatchException(e);
        }

        return e;
    }
}
