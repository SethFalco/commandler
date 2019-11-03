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

package org.elypia.commandler;

import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.managers.TestManager;
import org.elypia.commandler.metadata.*;

import javax.inject.*;
import java.util.*;

/**
 * The default help module, this is an optional module
 * one can add to provide some basic help functionality.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class DefaultHelpController implements Controller {

    private AppContext appContext;
    private TestManager testManager;

    @Inject
    public DefaultHelpController(AppContext appContext, TestManager testManager) {
        this.appContext = appContext;
        this.testManager = testManager;
    }

//    public String getGroups(ActionEvent<?, ?> event) {
//        StringJoiner joiner = new StringJoiner("\n", "Groups", "");
//
//        for (String group : appContext.getGroups(false).keySet())
//            joiner.add("* " + group);
//
//        return joiner.toString();
//    }
//
//    public String getModules(String query) {
//        var groups = appContext.getGroups(false);
//
//        Optional<String> optGroupName = groups.keySet().stream().filter(
//            name -> name.equalsIgnoreCase(query)
//        ).findAny();
//
//        if (optGroupName.isEmpty())
//            return "No such group exists.";
//
//        String groupName = optGroupName.get();
//        List<MetaController> modules = groups.get(groupName);
//        boolean disabled = false;
//
//        StringJoiner joiner = new StringJoiner("\n", groupName, "");
//
//        for (MetaController module : modules) {
//            StringBuilder builder = new StringBuilder("* ")
//                .append(module.toString());
//
//            if (testManager.isFailing(this)) {
//                builder.append(" *");
//                disabled = true;
//            }
//
//            builder
//                .append("\n")
//                .append(module.getDescription());
//
//            joiner.add(builder.toString());
//        }
//
//        if (disabled)
//            joiner.add("* Disabled due to live issues.");
//
//        return joiner.toString();
//    }

    /**
     * The default help command for a {@link Controller},
     * this should use the {@link MetaController} around
     * this {@link Controller} to display helpful information
     * to the user.
     *
     * @param event The {@link ActionEvent event} produced by Commandler.
     * @return The message to send to the end user.
     */
    public String getCommands(ActionEvent event, MetaController module) {
        StringBuilder builder = new StringBuilder(module.getName());

        builder
            .append(" (")
            .append(String.join(", "))
            .append(")\n")
            .append(module.getDescription());

        if (testManager.isFailing(this))
            builder.append("\n").append("This module is failing it's tests");

        builder.append("\n\n");

        Iterator<MetaCommand> metaCommandIt = module.getPublicCommands().iterator();

        while (metaCommandIt.hasNext()) {
            MetaCommand metaCommand = metaCommandIt.next();
            builder.append(metaCommand.getName());

            builder
                .append(" (")
                .append(String.join(", "))
                .append(")\n")
                .append(metaCommand.getDescription());

            List<MetaParam> metaParams = metaCommand.getMetaParams();

            metaParams.forEach((param) -> {
                builder.append("\n" + param.getName() + ": ");
                builder.append(param.getDescription());
            });

            if (metaCommandIt.hasNext())
                builder.append("\n\n");
        }

        return builder.toString();
    }
}
