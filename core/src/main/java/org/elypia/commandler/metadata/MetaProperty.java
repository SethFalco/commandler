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

package org.elypia.commandler.metadata;

/**
 * Metadata for properties that can be assigned to any
 * {@link MetaComponent}. These properties may represent
 * variables that impact how commands are processed.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public class MetaProperty {

    private String key;
    private String value;
    private boolean isPublic;
    private boolean isI18n;
    private String displayName;

    public MetaProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public MetaProperty(String key, String value, boolean isPublic, boolean isI18n) {
        this(key, value);
        this.isPublic = isPublic;
        this.isI18n = isI18n;
    }

    public MetaProperty(String key, String value, boolean isPublic, boolean isI18n, String displayName) {
        this(key, value, isPublic, isI18n);
        this.displayName = displayName;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isI18n() {
        return isI18n;
    }

    public String getDisplayName() {
        return displayName;
    }
}
