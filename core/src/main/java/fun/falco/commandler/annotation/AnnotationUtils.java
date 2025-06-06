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

package fun.falco.commandler.annotation;

/**
 * @author seth@falco.fun (Seth Falco)
 */
public final class AnnotationUtils {

    public static final String EFFECTIVELY_NULL = "fun.falco.commandler.NullValueMarker";

    private AnnotationUtils() {
        // Do nothing
    }

    public static boolean isEffectivelyNull(String value) {
        return EFFECTIVELY_NULL.equals(value);
    }

    public static String ifEffectivelyNull(String value) {
        return ifEffectivelyNull(value, null);
    }

    /**
     * @param value Key to check if {@link #isEffectivelyNull(String)}
     * @param elseValue Key to set if it is {@link #isEffectivelyNull(String)}.
     * @return Key provided provided if not null, else the else parameter.
     */
    public static String ifEffectivelyNull(String value, String elseValue) {
        return (isEffectivelyNull(value)) ? elseValue : value;
    }

    public static boolean isEffectivelyNull(String[] array) {
        return array.length == 1 && isEffectivelyNull(array[0]);
    }

    public static String[] ifEffectivelyNull(String[] value) {
        return ifEffectivelyNull(value, null);
    }

    public static String[] ifEffectivelyNull(String[] value, String[] elseValue) {
        return (isEffectivelyNull(value)) ? elseValue : value;
    }
}
