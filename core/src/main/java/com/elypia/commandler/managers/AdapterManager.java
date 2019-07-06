package com.elypia.commandler.managers;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.exceptions.*;
import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.*;
import com.google.inject.*;
import org.slf4j.*;

import java.lang.reflect.Array;
import java.util.*;

/**
 * <p>The {@link Param paramter} parser which is used to interpret
 * parameters specified in the {@link Command command} arguments.
 * Before a type can be used as an argument a {@link Adapter}
 * must be specified and registered to the {@link AdapterManager}.</p>
 * <p>The {@link AdapterManager} is how {@link String} input provided
 * by the chat client is converted into the respective object and passed
 * to the method associated with the {@link Command}.</p>
 */
public class AdapterManager {

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */
    private static final Logger logger = LoggerFactory.getLogger(AdapterManager.class);

    /** Used to manage dependency injection when constructions param adapters. */
    private final Injector injector;
    private final Collection<MetaAdapter> adapters;

    public AdapterManager(MetaAdapter... adapters) {
        this(Guice.createInjector(), adapters);
    }

    public AdapterManager(Injector injector, MetaAdapter... adapters) {
        this(injector, List.of(adapters));
    }

    public AdapterManager(Injector injector, Collection<MetaAdapter> adapters) {
        this.injector = Objects.requireNonNull(injector);
        this.adapters = Objects.requireNonNull(adapters);

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
    public Object[] adaptEvent(CommandlerEvent event) throws ListUnsupportedException, ParamParseException {
        Input input = event.getInput();
        List<MetaParam> params = input.getCommand().getParams();
        List<List<String>> inputs = input.getParams();

        List<Object> objects = new ArrayList<>();

        for (int i = 0; i < params.size(); i++) {
            MetaParam param = params.get(i);

            List<String> meh = (inputs.size() > i) ? inputs.get(i) : List.of(param.getDefaultValue());
            Object object = adaptParam(input, event, param, meh);
            objects.add(object);
        }

        Iterator<Object> iter = objects.iterator();
        List<Object> toReturn = new ArrayList<>();
        Class<?>[] types = input.getCommand().getMethod().getParameterTypes();

        for (Class<?> type : types) {
            if (CommandlerEvent.class.isAssignableFrom(type))
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
     * @param param The static parameter data associated with the parameter.
     * @param items The input provided by the user, this will only contain
     *              more than one item if the user provided a list of items.
     * @return      The parsed object as required for the command, or null
     *              if we failed to adapt the input. (Usually user misuse.)
     */
    protected Object adaptParam(Input input, CommandlerEvent event, MetaParam param, List<String> items) throws ParamParseException, ListUnsupportedException {
        Class<?> type = param.getType();
        Class<?> componentType = type.isArray() ? type.getComponentType() : type;
        ParamAdapter adapter = this.getAdapter(componentType);

        if (adapter == null)
            throw new RuntimeException(String.format("No adapters was created for the data-type %s.", componentType.getName()));

        int size = items.size();

        if (type.isArray()) {
            Object output = Array.newInstance(componentType, size);

            for (int i = 0; i < size; i++) {
                String item = items.get(i);
                Object o = adapter.adapt(item, componentType, param, event);

                if (o == null)
                    throw new ParamParseException(input, param, item);

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
                throw new ParamParseException(input, param, items.get(0));

            return o;
        }

        throw new ListUnsupportedException(input, param, items);
    }

    public ParamAdapter getAdapter(Class<?> typeRequired) {
        return getAdapter(typeRequired, Map.of());
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
    public <T> ParamAdapter<?> getAdapter(Class<?> typeRequired, Map<Class<T>, T> injectables) {
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

        Injector child = injector.createChildInjector(new AbstractModule() {

            @Override
            protected void configure() {
                injectables.forEach((key, value) -> bind(key).toInstance(value));
            }
        });

        return child.getInstance(adapter.getAdapterType());
    }
}
