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
import org.elypia.commandler.models.AllGroupsModel;

@MessageProvider(provides = String.class, value = AllGroupsModel.class)
public class AllGroupsMessenger implements Messenger<AllGroupsModel, String> {

    @Override
    public String provide(ActionEvent<?, String> event, AllGroupsModel groups) {
        StringBuilder builder = new StringBuilder("Groups");

        for (String group : groups.getGroups().keySet())
            builder.append("\n* ").append(group);

        return builder.toString();
    }
}
