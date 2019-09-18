package org.elypia.commandler.managers;

import org.elypia.commandler.api.*;
import org.elypia.commandler.event.*;
import org.elypia.commandler.exceptions.*;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.el.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * <p>The {@link MetaParam paramter} parser which is used to interpret
 * parameters specified in the {@link MetaCommand command} arguments.
 * Before a type can be used as an argument a {@link Adapter}
 * must be specified and registered to the {@link AdapterManager}.</p>
 * <p>The {@link AdapterManager} is how {@link String} input provided
 * by the chat client is converted into the respective object and passed
 * to the method associated with the {@link MetaCommand}.</p>
 */
public class AdapterManager {

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */
    private static final Logger logger = LoggerFactory.getLogger(AdapterManager.class);

    /** Used to manage dependency injection when constructions param adapters. */
    private final InjectionManager injectionManager;
    private final Collection<MetaAdapter> adapters;
    private final ExpressionFactory expressionFactory;

    public AdapterManager(InjectionManager injectionManager, MetaAdapter... adapters) {
        this(injectionManager, List.of(adapters));
    }

    public AdapterManager(InjectionManager injectionManager, Collection<MetaAdapter> adapters) {
        this.injectionManager = Objects.requireNonNull(injectionManager);
        this.adapters = Objects.requireNonNull(adapters);
        this.expressionFactory = ELManager.getExpressionFactory();

        for (MetaAdapter adapter : adapters)
            logger.debug("Adapter added for type {}.", adapter.getAdapterType().getSimpleName());
    }

    /**
     * Take the String parameters from the message event and adapt them into the required
     * format the commands method requires to execute.
     *
     * @param event The message event to take parameters from.
     * @return An array of all parameters adapted as required for the given method.
     */
    public Object[] adaptEvent(ActionEvent event) throws ListUnsupportedException, ParamParseException {
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
                // TODO: This is reconstructer for each optional parameter, this only needs to be done once.
                // TODO: Make it possible for a this to use previous parameters.
                ELContext context = new StandardELContext(expressionFactory);
                VariableMapper mapper = context.getVariableMapper();
                mapper.setVariable("e", expressionFactory.createValueExpression(event, ActionEvent.class));
                mapper.setVariable("a", expressionFactory.createValueExpression(event.getAction(), Action.class));
                mapper.setVariable("c", expressionFactory.createValueExpression(event.getIntegration(), Integration.class));
                mapper.setVariable("s", expressionFactory.createValueExpression(event.getSource(), event.getSource().getClass()));

                String defaultValue = metaParam.getDefaultValue();
                ValueExpression ve = expressionFactory.createValueExpression(context, defaultValue, Object.class);
                Object value = ve.getValue(context);
                Class<?> type = value.getClass();

                if (String.class.isAssignableFrom(type))
                    param = List.of((String)value);
                else if (String[].class.isAssignableFrom(type))
                    param = List.of((String[])value);
                else if (List.class.isAssignableFrom(type))
                    param = (List<String>)value; // TODO: Only checking if it's a list, not List<String>
                else if (metaParam.getType().isAssignableFrom(type)) {
                    objects.add(value);
                    continue;
                }
                else
                    throw new RuntimeException("defaultValue must be assignable to String, String[], List<String> or " + metaParam.getType().getSimpleName() + ".");

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
     * This actually convertes an individual parameter into the type
     * required for a command. If the type required is an array,
     * we convert each item in the array using the
     * {@link Class#getComponentType() component type}. <br>
     * This should return null if a parameter fails to adapt or list
     * parameter is provided where lists are not supported.
     *
     * @param event The message event to take parameters from.
     * @param metaParam The static parameter data associated with the parameter.
     * @param items The input provided by the user, this will only contain
     *              more than one item if the user provided a list of items.
     * @return      The parsed object as required for the command, or null
     *              if we failed to adapt the input. (Usually user misuse.)
     */
    protected Object adaptParam(Action action, ActionEvent event, MetaParam metaParam, List<String> items) throws ParamParseException, ListUnsupportedException {
        Class<?> type = metaParam.getType();
        Class<?> componentType = type.isArray() ? type.getComponentType() : type;
        Adapter adapter = this.getAdapter(componentType);

        if (adapter == null)
            throw new RuntimeException(String.format("No adapters was created for the data-type %s.", componentType.getName()));

        int size = items.size();

        if (type.isArray()) {
            Object output = Array.newInstance(componentType, size);

            for (int i = 0; i < size; i++) {
                String item = items.get(i);
                Object o = adapter.adapt(item, componentType, metaParam, event);

                if (o == null)
                    throw new ParamParseException(event, metaParam, item);

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
            Object o = adapter.adapt(items.get(0), componentType, metaParam, event);

            if (o == null)
                throw new ParamParseException(event, metaParam, items.get(0));

            return o;
        }

        throw new ListUnsupportedException(event, metaParam, items);
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

        for (MetaAdapter metaAdapter : adapters) {
            Collection<Class<?>> compatible = metaAdapter.getCompatibleTypes();

            if (compatible.contains(typeRequired)) {
                adapter = metaAdapter;
                break;
            }

            if (compatible.stream().anyMatch(c -> c.isAssignableFrom(typeRequired)))
                adapter = metaAdapter;
        }

        if (adapter == null)
            throw new AdapterRequiredException("Adapter required for type " + typeRequired + ".");

        logger.debug("Using `{}` adapter for parameter.", adapter.getAdapterType().getSimpleName());
        // TODO: Add event objects and params to child injector
        return injectionManager.getInjector().getInstance(adapter.getAdapterType());
    }
}
