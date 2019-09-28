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

package org.elypia.commandler.config;

import org.apache.commons.configuration2.builder.DefaultParametersHandler;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;

import java.nio.charset.StandardCharsets;

/**
 * Load all files as {@link StandardCharsets#UTF_8}.
 *
 * @author seth@elypia.org (Syed Shah)
 */
public class FileDefaultsHandler implements DefaultParametersHandler<FileBasedBuilderParameters> {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @Override
    public void initializeDefaults(FileBasedBuilderParameters parameters) {
        parameters.setEncoding(UTF_8);
    }
}
