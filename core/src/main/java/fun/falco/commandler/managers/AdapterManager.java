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

package fun.falco.commandler.managers;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.el.ELContext;
import javax.el.ELManager;
import javax.el.ExpressionFactory;
import javax.el.StandardELContext;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import fun.falco.commandler.Commandler;
import fun.falco.commandler.CommandlerExtension;
import fun.falco.commandler.api.Adapter;
import fun.falco.commandler.api.Integration;
import fun.falco.commandler.event.Action;
import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.exceptions.AdapterRequiredException;
import fun.falco.commandler.exceptions.misuse.ListUnsupportedException;
import fun.falco.commandler.exceptions.misuse.ParamParseException;
import fun.falco.commandler.metadata.MetaAdapter;
import fun.falco.commandler.metadata.MetaParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for parameter adapters. Parses parameter values from incoming
 * requests. Before a data-type can be used a parameter, an {@link Adapter} must
 * be implemented and registered with {@link Commandler}.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
public class AdapterManager {

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */
    private static final Logger logger = LoggerFactory.getLogger(AdapterManager.class);

    /** The configuration class which contains all metadata for this instance. */
    private final CommandlerExtension extension;

    /** Used to evaluate expressions and build default values for parameters. */
    private final ExpressionFactory expressionFactory;

    @Inject
    public AdapterManager(final CommandlerExtension extension) {
        this.extension = Objects.requireNonNull(extension);
        this.expressionFactory = ELManager.getExpressionFactory();
    }

    /**
     * Take the String parameters from the message event and adapt them into the required
     * format the commands method requires to execute.
     *
     * @param event Message event to take parameters from.
     * @return Array of all parameters adapted as required for the given method.
     */
    public Object[] adaptEvent(ActionEvent event) {
        Action action = event.getAction();
        List<MetaParam> metaParams = event.getMetaCommand().getMetaParams();
        Iterator<List<String>> userInputParameters = action.getParams().iterator();
        Class<?>[] types = event.getMetaCommand().getMethod().getParameterTypes();
        Object[] itemsToReturn = new Object[types.length];

        for (int i = 0; i < types.length; i++) {
            final int iFinal = i;

            Optional<MetaParam> optMetaParam = metaParams.stream()
                .filter((m) -> m.getMethodIndex() == iFinal)
                .findAny();

            if (optMetaParam.isEmpty()) {
                itemsToReturn[i] = BeanProvider.getContextualReference(types[i]);
            } else {
                MetaParam metaParam = optMetaParam.get();
                List<String> param;

                if (userInputParameters.hasNext()) {
                    param = userInputParameters.next();
                } else {
                    ELContext context = new StandardELContext(expressionFactory);
                    VariableMapper mapper = context.getVariableMapper();
                    mapper.setVariable("event", expressionFactory.createValueExpression(event, ActionEvent.class));
                    mapper.setVariable("action", expressionFactory.createValueExpression(action, Action.class));
                    mapper.setVariable("integration", expressionFactory.createValueExpression(event.getRequest().getIntegration(), Integration.class));
                    mapper.setVariable("source", expressionFactory.createValueExpression(event.getRequest().getSource(), event.getRequest().getSource().getClass()));

                    String defaultValue = metaParam.getDefaultValue();
                    ValueExpression ve = expressionFactory.createValueExpression(context, defaultValue, Object.class);
                    Object value = ve.getValue(context);
                    Class<?> type = value.getClass();
                    Class<?> parameterType = metaParam.getParameter().getType();

                    if (String.class.isAssignableFrom(type)) {
                        param = List.of((String) value);
                    } else if (String[].class.isAssignableFrom(type)) {
                        param = List.of((String[]) value);
                    } else if (List.class.isAssignableFrom(type)) {
                        param = (List<String>) value;
                    } else if (parameterType.isAssignableFrom(type)) {
                        itemsToReturn[i] = value;
                        continue;
                    } else {
                        throw new RuntimeException("defaultValue must be assignable to String, String[], List<String> or " + parameterType + ".");
                    }
                }

                Object object = adaptParam(action, event, metaParam, param);
                itemsToReturn[i] = object;
            }
        }

        return itemsToReturn;
    }

    /**
     * This actually converts an individual param into the type required for a
     * command. If the type required is an array, we convert each item in the
     * array using the {@link Class#getComponentType() component type}.
     *
     * <p>This should return null if a parameter fails to adapt or list
     * parameter is provided where lists are not supported.</p>
     *
     * @param action User action that required this parsing.
     * @param event Message event to take parameters from.
     * @param param Static parameter data associated with the parameter.
     * @param items
     *     Input provided by the user, this will only contain more than one item
     *     if the user provided a list of items.
     * @return
     *     Parsed object as required for the command, or null if we failed to
     *     adapt the input. (Usually user misuse.)
     */
    protected Object adaptParam(Action action, ActionEvent event, MetaParam param, List<String> items) {
        Class<?> type = param.getParameter().getType();
        Class<?> componentType = type.isArray() ? type.getComponentType() : type;
        Adapter adapter = this.getAdapter(componentType);

        if (adapter == null) {
            throw new RuntimeException(String.format("No adapters was created for the data-type %s.", componentType.getName()));
        }

        int size = items.size();

        if (type.isArray()) {
            Object output = Array.newInstance(componentType, size);

            for (int i = 0; i < size; i++) {
                String item = items.get(i);
                Object o = adapter.adapt(item, componentType, param, event);

                if (o == null) {
                    throw new ParamParseException(event, param, item);
                }

                if (componentType == boolean.class) {
                    Array.setBoolean(output, i, (boolean) o);
                } else if (componentType == char.class) {
                    Array.setChar(output, i, (char) o);
                } else if (componentType == double.class) {
                    Array.setDouble(output, i, (double) o);
                } else if (componentType == float.class) {
                    Array.setFloat(output, i, (float) o);
                } else if (componentType == long.class) {
                    Array.setLong(output, i, (long) o);
                } else if (componentType == int.class) {
                    Array.setInt(output, i, (int) o);
                } else if (componentType == short.class) {
                    Array.setShort(output, i, (short) o);
                } else if (componentType == byte.class) {
                    Array.setByte(output, i, (byte) o);
                } else {
                    Array.set(output, i, o);
                }
            }

            return output;
        }

        if (size == 1) {
            Object o = adapter.adapt(items.get(0), componentType, param, event);

            if (o == null) {
                throw new ParamParseException(event, param, items.get(0));
            }

            return o;
        }

        throw new ListUnsupportedException(event, param, items);
    }

    /**
     * Iterate adapters and get the most appropriate one for to adapt this type.
     *
     * <p>Starts by looking for one with an exact compatible type, if not found
     * then resorts to searching for adapters that work with a
     * {@link Class#isAssignableFrom(Class)} with a compatible class.
     * <strong>It's favorable not to rely on
     * {@link Class#isAssignableFrom(Class)} where possible.</strong></p>
     *
     * @param typeRequired Type that needs adapting.
     * @return Adapter for this data into the required type.
     */
    public <T> Adapter<?> getAdapter(Class<?> typeRequired) {
        MetaAdapter adapter = null;

        for (MetaAdapter metaAdapter : extension.getMetaAdapters()) {
            Collection<Class<?>> compatible = metaAdapter.getCompatibleTypes();

            if (compatible.contains(typeRequired)) {
                adapter = metaAdapter;
                break;
            }

            if (compatible.stream().anyMatch(c -> c.isAssignableFrom(typeRequired))) {
                adapter = metaAdapter;
            }
        }

        if (adapter == null) {
            throw new AdapterRequiredException("Adapter required for type " + typeRequired + ".");
        }

        logger.debug("Using `{}` to parse parameter.", adapter.getAdapterType());
        return BeanProvider.getContextualReference(adapter.getAdapterType());
    }
}
