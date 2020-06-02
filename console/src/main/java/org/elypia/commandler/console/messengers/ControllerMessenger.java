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

package org.elypia.commandler.console.messengers;

import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.api.Messenger;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.models.*;

import java.util.*;

@MessageProvider(provides = String.class, value = ControllerModel.class)
public class ControllerMessenger implements Messenger<ControllerModel, String> {

    @Override
    public String provide(ActionEvent<?, String> event, ControllerModel controller) {
        StringBuilder builder = new StringBuilder(controller.getName())
            .append("\n")
            .append(controller.getDescription());

//        appendActivators(builder, controller);
        builder.append("\n\n");

        Iterator<CommandModel> commands = controller.getCommands().iterator();

        while (commands.hasNext()) {
            CommandModel metaCommand = commands.next();
            builder.append(metaCommand.getName())
                .append("\n")
                .append(metaCommand.getDescription());
//            appendActivators(builder, metaCommand);
            List<ParamModel> params = metaCommand.getParams();

            params.forEach((param) -> {
                builder.append("\n" + param.getName() + ": ");
                builder.append(param.getDescription());
            });

            if (commands.hasNext())
                builder.append("\n\n");
        }

        return builder.toString();
    }
}
