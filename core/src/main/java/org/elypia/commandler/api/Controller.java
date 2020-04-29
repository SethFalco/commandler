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

package org.elypia.commandler.api;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public interface Controller {

    /**
     * Performs relevent tests to ensure this module
     * is still working as intended. Commandler
     * will disable the module until the test passes again
     * if it fails.
     *
     * @return If the module should remain enabled.
     */
    default boolean test() {
        return true;
    }
}
