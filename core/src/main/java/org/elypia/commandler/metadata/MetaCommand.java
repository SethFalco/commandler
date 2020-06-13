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

import org.slf4j.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class MetaCommand extends MetaComponent implements Comparable<MetaCommand>, Iterable<MetaParam> {

    /** We use SLF4J for logging, be sure to include an implementation / binding. */
    private static final Logger logger = LoggerFactory.getLogger(MetaCommand.class);

    /** The actual method that is called when this command is performed. */
    private Method method;

    /** If this command is hidden from public help messages. */
    private boolean isHidden;

    /** The parameters this command requires. */
    private List<MetaParam> metaParams;

    public MetaCommand(Method method, String name, String help, boolean isHidden, Map<String, MetaProperty> properties, List<MetaParam> metaParams) {
        this.method = Objects.requireNonNull(method);
        this.name = Objects.requireNonNull(name);
        this.description = help;
        this.isHidden = isHidden;
        this.properties = properties;
        this.metaParams = Objects.requireNonNull(metaParams);
    }

    public int getMinParams() {
        return (int) metaParams.stream().filter(MetaParam::isRequired).count();
    }

    public int getMaxParams() {
        return metaParams.size();
    }

    /**
     * Returns if the number provided is a suitable number of
     * parameters for this command.
     *
     * @param count The number to check against.
     * @return If this number represents an appropriate number of params
     * to perform this command.
     */
    public boolean isValidParamCount(int count) {
        long required = getMinParams();
        int total = getMaxParams();

        return count >= required && count <= total;
    }

    public Method getMethod() {
        return method;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isPublic() {
        return !isHidden;
    }

    public List<MetaParam> getMetaParams() {
        return Collections.unmodifiableList(metaParams);
    }

    @Override
    public String toString() {
        return this.getClass() + " | " + toParamString();
    }

    /**
     * @return The {@link #metaParams params} names displayed
     * in a user friendly that shows more info about them inline.
     */
    public String toParamString() {
        StringJoiner itemJoiner = new StringJoiner(" ");

        for (MetaParam metaParam : metaParams) {
            Class<?> clazz = metaParam.getParameter().getType();
            StringBuilder builder = new StringBuilder();

            if (metaParam.isOptional())
                builder.append("?");

            if (metaParam.isList())
                builder.append("[").append(clazz).append("]");
            else
                builder.append(clazz);

            itemJoiner.add(builder.toString());
        }

        return "(" + metaParams.size() + ") " + itemJoiner.toString();
    }

    /**
     * Sorts {@link MetaCommand}s into alphabetical order.
     *
     * @param o Another command.
     * @return If this command is above or below the provided command.
     */
    @Override
    public int compareTo(MetaCommand o) {
        return name.compareToIgnoreCase(o.name);
    }

    @Override
    public Iterator<MetaParam> iterator() {
        return metaParams.iterator();
    }
}
