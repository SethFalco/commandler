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

package fun.falco.commandler.groups;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fun.falco.commandler.annotation.Group;

/**
 * Default guidance group, this group should be applied to informational modules
 * such as help, instructions, interactive guides, privacy policy,  or any other
 * more technical module to help users use the bot.
 *
 * <p>This is standardized in Commandler for easier integration between
 * libraries and extensions.</p>
 *
 * <p>If the default localized names for the {@link Guidance} are undesirable,
 * they can be overridden in your own <code>GroupMessages.properties</code>.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 * @since 4.1.0
 */
@Group
@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Guidance {

}
