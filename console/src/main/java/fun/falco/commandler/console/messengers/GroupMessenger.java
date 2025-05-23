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

package fun.falco.commandler.console.messengers;

import fun.falco.commandler.annotation.stereotypes.MessageProvider;
import fun.falco.commandler.api.Messenger;
import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.models.ControllerModel;
import fun.falco.commandler.models.GroupModel;

@MessageProvider(provides = String.class, value = GroupModel.class)
public class GroupMessenger implements Messenger<GroupModel, String> {

    @Override
    public String provide(ActionEvent<?, String> event, GroupModel group) {
        StringBuilder builder = new StringBuilder();

        for (ControllerModel controller : group) {
            builder.append(controller.getName()).append("\n").append(controller.getDescription());
            builder.append("\n\n");
        }

        return builder.toString();
    }
}
