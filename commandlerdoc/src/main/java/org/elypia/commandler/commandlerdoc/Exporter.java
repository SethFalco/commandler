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

package org.elypia.commandler.commandlerdoc;

import java.io.IOException;

import org.elypia.commandler.commandlerdoc.models.ExportableData;

/**
 * Defines a class that is capable of exporting data.
 *
 * @author seth@falco.fun (Seth Falco)
 * @since 4.0.2
 */
@FunctionalInterface
public interface Exporter {

    /**
     * Perform the implementations export operation.
     *
     * @param data
     *     Exportable data that needs to be transformed into the desired format.
     * @throws IOException If an exceptions occurs while writing the data.
     */
    void export(ExportableData data) throws IOException;
}
