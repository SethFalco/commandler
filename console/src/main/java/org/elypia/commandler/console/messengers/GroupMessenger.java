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

@MessageProvider(provides = String.class, value = GroupModel.class)
public class GroupMessenger implements Messenger<GroupModel, String> {

    @Override
    public String provide(ActionEvent<?, String> event, GroupModel group) {
        StringBuilder builder = new StringBuilder();

        for (ControllerModel controller : group) {
            builder.append(controller.getName()).append("\n").append(controller.getDescription());
//            appendActivators(builder, controller);
            builder.append("\n\n");
        }

        return builder.toString();
    }
}
