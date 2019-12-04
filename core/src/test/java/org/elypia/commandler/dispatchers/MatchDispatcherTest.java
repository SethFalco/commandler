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

package org.elypia.commandler.dispatchers;

import org.elypia.commandler.Request;
import org.elypia.commandler.api.Integration;
import org.elypia.commandler.config.ControllerConfig;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
class MatchDispatcherTest {

    private MetaCommand command;
    private MetaController controller;
    private ControllerConfig controllerConfig;
    private Integration<String, String> integration;

    @BeforeEach
    public void beforeEach() {
        MetaParam value = mock(MetaParam.class);
        when(value.getIndex()).thenReturn(0);
        when(value.getName()).thenReturn("value");
        when(value.getDescription()).thenReturn("The value of the unit to convert.");

        MetaParam unit = mock(MetaParam.class);
        when(value.getIndex()).thenReturn(1);
        when(value.getName()).thenReturn("unit");
        when(value.getDescription()).thenReturn("The unit the user is converting for, KG or Lbs.");

        command = mock(MetaCommand.class);
        when(command.getName()).thenReturn("Convert Weight");
        when(command.getDescription()).thenReturn("Convert weight between KG and lbs for users.");
        when(command.getProperty(any(Class.class), anyString())).thenReturn("(?i)\\b([\\d,.]+)\\h*(KG|LBS?)\\b");
        when(command.getMetaParams()).thenReturn(List.of(value, unit));
        when(command.getMinParams()).thenReturn(2);
        when(command.getMaxParams()).thenReturn(2);

        controller = mock(MetaController.class);
        when(controller.getName()).thenReturn("Conversion");
        when(controller.getDescription()).thenReturn("Convert units like weight, or lengths.");
        when(controller.getMetaCommands()).thenReturn(List.of(command));
        when(controller.isHidden()).thenReturn(false);
        when(controller.isPublic()).thenReturn(true);

        controllerConfig = mock(ControllerConfig.class);
        when(controllerConfig.getControllers()).thenReturn(List.of(controller));

        integration = mock(Integration.class);
        when(integration.getActionId(anyString())).thenReturn(1);
        when(integration.getMessageType()).thenReturn(String.class);
    }

    @Test
    public void isValid() {
        MatchDispatcher dispatcher = new MatchDispatcher(controllerConfig);
        String content = "I weight 103KG!";
        Request<String, String> request = new Request<>(integration, content, content, content);

        boolean actual = dispatcher.isValid(request);
        assertTrue(actual);
    }

    @Test
    public void doesParseMatchWithoutException() {
        when(command.isValidParamCount(anyInt())).thenReturn(true);
        MatchDispatcher dispatcher = new MatchDispatcher(controllerConfig);
        String content = "I weight 103KG!";
        Request<String, String> request = new Request<>(integration, content, content, content);

        ActionEvent event = dispatcher.parse(request);
        assertNotNull(event);
    }

    @Test
    public void doesHandleNoMatchCorrectly() {
        when(command.isValidParamCount(anyInt())).thenReturn(true);
        MatchDispatcher dispatcher = new MatchDispatcher(controllerConfig);
        String content = "I do not fit any of the patterns!";
        Request<String, String> request = new Request<>(integration, content, content, content);

        ActionEvent event = dispatcher.parse(request);
        assertNull(event);
    }

    @Test
    public void doesHandleMatchCorrectly() {
        when(command.isValidParamCount(anyInt())).thenReturn(true);
        MatchDispatcher dispatcher = new MatchDispatcher(controllerConfig);
        String content = "I weight 103KG!";
        Request<String, String> request = new Request<>(integration, content, content, content);

        ActionEvent event = dispatcher.parse(request);
        assertEquals("(1) 103 (2) KG", event.getAction().toParamString());
    }
}
