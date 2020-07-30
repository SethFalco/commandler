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

/**
 * Sensible default grouping annotations that can be applied
 * to {@link org.elypia.commandler.annotation.stereotypes.Controller}
 * classes.
 *
 * <pre><code>
 * {@literal @}Miscellaneous
 * {@literal @}StandardController
 *  public class UtilityController {
 *
 *     {@literal @}StandardCommand
 *      public int sum({@literal @}Param int x, {@literal @}Param int y) {
 *          return x + y;
 *      }
 *  }</code></pre>
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 4.1.0
 */
package org.elypia.commandler.groups;
