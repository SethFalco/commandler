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

package org.elypia.commandler;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.elypia.commandler.exceptions.misuse.NoDefaultCommandException;
import org.elypia.commandler.metadata.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DefaultMisuseHandlerTest {

    private MetaController controller;

    @BeforeEach
    public void beforeEach() {
        MetaCommand command = mock(MetaCommand.class);
        when(command.toParamString()).thenReturn("[words] ?random");
        when(command.toString()).thenReturn("Define | [words] ?random");

        controller = mock(MetaController.class);
        when(controller.getName()).thenReturn("Urban Dictionary");
        when(controller.getPublicCommands()).thenReturn(List.of(command));
    }

    @Test
    public void testNoDefaultCommand() {
        DefaultMisuseHandler handler = new DefaultMisuseHandler();
        NoDefaultCommandException ex = new NoDefaultCommandException(controller);

        String expected =
            "Command failed; this module has no default command.\n" +
            "Module: Urban Dictionary\n" +
            "\n" +
            "Possibilities:\n" +
            "Define | [words] ?random\n" +
            "\n" +
            "See the help command for more information.";

        ExceptionEvent<NoDefaultCommandException> event = mock(ExceptionEvent.class);
        when(event.getException()).thenReturn(ex);

        String actual = handler.onNoDefaultCommand(event);

        assertEquals(expected, actual);
    }
}
