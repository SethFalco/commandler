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
 * Models have the sole purpose of being easy to transform for messengers.
 * They don't use any Commandler, or CDI magic, they're just useful
 * intermediate objects that have {@link org.elypia.commandler.api.Messenger}s
 * implemented amongst multiple integrations.
 */
package org.elypia.commandler.models;
