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

package org.elypia.commandler.groups;

import org.elypia.commandler.annotation.Group;

import java.lang.annotation.*;

/**
 * The default guidance group, this group should be applies to infomational
 * modules such as help, instructions, interactive guides, privacy policy,
 * or any other more technical module to help users use the bot.
 *
 * This is standardised in Commandler for easier integration between
 * libraries and extensions.
 *
 * If the default localized names for the {@link Guidance} are undiresable,
 * they can be overridden in your own <code>GroupMessages.properties</code>.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 4.1.0
 */
@Group
@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Guidance {

}
