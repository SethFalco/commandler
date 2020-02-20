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

package org.elypia.commandler.managers;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.elypia.commandler.Commandler;
import org.elypia.commandler.api.*;
import org.elypia.commandler.config.CommandlerConfig;
import org.elypia.commandler.event.*;
import org.elypia.commandler.exceptions.AdapterRequiredException;
import org.elypia.commandler.exceptions.misuse.*;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.el.*;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.lang.reflect.Array;
import java.util.*;

/**
 * <p>The {@link MetaParam param} adapter which is used to interpret
 * params specified in the {@link MetaCommand commands}.
 * Before a type can be used as an param an {@link Adapter}
 * must be specified and registered to the {@link Commandler}.</p>
 *
 * <p>The {@link AdapterManager} is how {@link String} input provided
 * by the chat client is converted into the respective object and passed
 * to the method associated with the {@link MetaCommand}.</p>
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class AdapterManager {

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */
    private static final Logger logger = LoggerFactory.getLogger(AdapterManager.class);

    private final BeanManager beanManager;

    /** The configuration class which contains all metadata for this instance. */
    private final CommandlerConfig commandlerConfig;

    /** Used to evaluate expressions and build default values for parameters. */
    private final ExpressionFactory expressionFactory;

    /**
     * @param commandlerConfig Main commandler configuration.
     */
    @Inject
    public AdapterManager(final BeanManager beanManager, final CommandlerConfig commandlerConfig) {
        this.beanManager = beanManager;
        this.commandlerConfig = Objects.requireNonNull(commandlerConfig);
        this.expressionFactory = ELManager.getExpressionFactory();
    }

    /**
     * Take the String parameters from the message event and adapt them into the required
     * format the commands method requires to execute.
     *
     * @param event The message event to take parameters from.
     * @return An array of all parameters adapted as required for the given method.
     */
    public Object[] adaptEvent(ActionEvent event) {
        Action input = event.getAction();
        List<MetaParam> metaParams = event.getMetaCommand().getMetaParams();
        List<List<String>> inputs = input.getParams();
        List<Object> objects = new ArrayList<>();

        for (int i = 0; i < metaParams.size(); i++) {
            MetaParam metaParam = metaParams.get(i);
            List<String> param;

            if (inputs.size() > i)
                param = inputs.get(i);
            else {
                ELContext context = new StandardELContext(expressionFactory);
                VariableMapper mapper = context.getVariableMapper();
                mapper.setVariable("e", expressionFactory.createValueExpression(event, ActionEvent.class));
                mapper.setVariable("a", expressionFactory.createValueExpression(event.getAction(), Action.class));
                mapper.setVariable("c", expressionFactory.createValueExpression(event.getRequest().getIntegration(), Integration.class));
                mapper.setVariable("s", expressionFactory.createValueExpression(event.getRequest().getSource(), event.getRequest().getClass()));

                String defaultValue = metaParam.getDefaultValue();
                ValueExpression ve = expressionFactory.createValueExpression(context, defaultValue, Object.class);
                Object value = ve.getValue(context);
                Class<?> type = value.getClass();
                Class<?> parameterType = metaParam.getParameter().getType();

                if (String.class.isAssignableFrom(type))
                    param = List.of((String)value);
                else if (String[].class.isAssignableFrom(type))
                    param = List.of((String[])value);
                else if (List.class.isAssignableFrom(type))
                    param = (List<String>)value;
                else if (parameterType.isAssignableFrom(type)) {
                    objects.add(value);
                    continue;
                }
                else
                    throw new RuntimeException("defaultValue must be assignable to String, String[], List<String> or " + parameterType + ".");

            }

            Object object = adaptParam(input, event, metaParam, param);
            objects.add(object);
        }

        Iterator<Object> iter = objects.iterator();
        List<Object> toReturn = new ArrayList<>();
        Class<?>[] types = event.getMetaCommand().getMethod().getParameterTypes();

        for (Class<?> type : types) {
            if (ActionEvent.class.isAssignableFrom(type))
                toReturn.add(event);
            else
                toReturn.add(iter.next());
        }

        return Collections.unmodifiableList(toReturn).toArray();
    }

    /**
     * <p>This actually converts an individual param into the type
     * required for a command. If the type required is an array,
     * we convert each item in the array using the
     * {@link Class#getComponentType() component type}.</p>
     *
     * <p>This should return null if a parameter fails to adapt or list
     * parameter is provided where lists are not supported.</p>
     *
     * @param event The message event to take parameters from.
     * @param param The static parameter data associated with the parameter.
     * @param items The input provided by the user, this will only contain
     * more than one item if the user provided a list of items.
     * @return The parsed object as required for the command, or  null
     * if we failed to adapt the input. (Usually user misuse.)
     */
    protected Object adaptParam(Action action, ActionEvent event, MetaParam param, List<String> items) {
        Class<?> type = param.getParameter().getType();
        Class<?> componentType = type.isArray() ? type.getComponentType() : type;
        Adapter adapter = this.getAdapter(componentType);

        if (adapter == null)
            throw new RuntimeException(String.format("No adapters was created for the data-type %s.", componentType.getName()));

        int size = items.size();

        if (type.isArray()) {
            Object output = Array.newInstance(componentType, size);

            for (int i = 0; i < size; i++) {
                String item = items.get(i);
                Object o = adapter.adapt(item, componentType, param, event);

                if (o == null)
                    throw new ParamParseException(event, param, item);

                if (componentType == boolean.class)
                    Array.setBoolean(output, i, (boolean)o);
                else if (componentType == char.class)
                    Array.setChar(output, i, (char)o);
                else if (componentType == double.class)
                    Array.setDouble(output, i, (double)o);
                else if (componentType == float.class)
                    Array.setFloat(output, i, (float)o);
                else if (componentType == long.class)
                    Array.setLong(output, i, (long)o);
                else if (componentType == int.class)
                    Array.setInt(output, i, (int)o);
                else if (componentType == short.class)
                    Array.setShort(output, i, (short)o);
                else if (componentType == byte.class)
                    Array.setByte(output, i, (byte)o);
                else
                    Array.set(output, i, o);
            }

            return output;
        }

        if (size == 1) {
            Object o = adapter.adapt(items.get(0), componentType, param, event);

            if (o == null)
                throw new ParamParseException(event, param, items.get(0));

            return o;
        }

        throw new ListUnsupportedException(event, param, items);
    }

    /**
     * <p>Iterate adapters and get the most appropriate one
     * for to adapt this type.</p>
     * <p>Starts by looking for one with an exact compatible type,
     * if not found then resorts to searching for adapters that work with
     * a {@link Class#isAssignableFrom(Class)} with a compatible class.
     * <strong>It's favourable not to rely on {@link Class#isAssignableFrom(Class)}
     * where possible.</strong>
     *
     * @param typeRequired The type that needs adapting.
     * @return The adapter for this data into the required type.
     */
    public <T> Adapter<?> getAdapter(Class<?> typeRequired) {
        MetaAdapter adapter = null;

        for (MetaAdapter metaAdapter : commandlerConfig.getAdapters()) {
            Collection<Class<Object>> compatible = metaAdapter.getCompatibleTypes();

            if (compatible.contains(typeRequired)) {
                adapter = metaAdapter;
                break;
            }

            if (compatible.stream().anyMatch(c -> c.isAssignableFrom(typeRequired)))
                adapter = metaAdapter;
        }

        if (adapter == null)
            throw new AdapterRequiredException("Adapter required for type " + typeRequired + ".");

        logger.debug("Using `{}` to parse parameter.", adapter.getAdapterType());
        return BeanProvider.getContextualReference(adapter.getAdapterType());
    }
}
