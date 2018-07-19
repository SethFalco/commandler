package com.elypia.commandler.components;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.parsers.*;
import org.slf4j.*;

import java.lang.reflect.Array;
import java.net.URL;
import java.time.*;
import java.util.*;

/**
 * The {@link Param paramter} parser which is used to interpret
 * parameters specified in the {@link Command command} arguments.
 * Before a data-type can be used as an argument a {@link IParser parser}
 * must be specified and registered to the {@link Parsers} via the
 * {@link Commandler#registerParser(IParser, Class[])} method. <br>
 * The {@link Parsers} is how {@link String} input provided
 * by the chat client is converted into the respective object and passed
 * to the method associated with the {@link Command}.
 */

public class Parsers implements Iterable<IParser<?>> {

    /**
     * This is logged whenever a new {@link IParser} is registered.
     */

    private static final String REGISTERED_PARSER = "Registered the {} parser for the data-type {}.";

    /**
     * This is logged whenever a existing {@link IParser} for a
     * particular data-type is replaced with a new one.
     */

    private static final String REPLACED_PARSER = "The parser, {}, for the data-type {} has been replaced with {}.";

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */

    private static final Logger logger = LoggerFactory.getLogger(Parsers.class);

    /**
     * Our parent Commandler instance which receives and manages the events.
     */

    protected Commandler commandler;

    /**
     * The associated configuration for our {@link #commandler} instance.
     */

    protected IConfiler confiler;

    /**
     * The registered parsers which allow us to use particular data-types
     * as {@link Command} {@link Param parameters}.
     */

    protected Map<Class<?>, IParser<?>> parsers;

    public Parsers(Commandler commandler) {
        this.commandler = Objects.requireNonNull(commandler);
        confiler = commandler.getConfiler();
        parsers = new HashMap<>();

        // ? Register default parsers provided by Commandler
        registerParser(new StringParser(), String.class);
        registerParser(new NumberParser(), NumberParser.TYPES);
        registerParser(new BooleanParser(), Boolean.class, boolean.class);
        registerParser(new UrlParser(), URL.class);
        registerParser(new DurationParser(), Duration.class);
        registerParser(new EnumParser(), Enum.class);
    }

    /**
     * Register a new {@link IParser}. This will allow {@link Commandler} to
     * parse {@link String} input from the {@link CommandInput} provided
     * by the user into the respective arguments required by the {@link Command}. <br>
     * {@link Commandler} may register a few {@link IParser parsers} by default for
     * common types and primitives.
     *
     * @param newParser The parser we're registering.
     * @param types The types of objects we wish to use this parser for.
     *              (When selecting a parser, we look a {@link IParser} of
     *              the same type first, else an
     *              {@link Class#isAssignableFrom(Class) assignable} type.
     */

    public void registerParser(IParser newParser, Class...types) {
        for (Class type : types) {
            IParser<?> oldParser = parsers.put(type, newParser);

            if (oldParser == null)
                logger.debug(REGISTERED_PARSER, newParser.getClass().getName(), type.getName());
            else
                logger.info(REPLACED_PARSER, oldParser.getClass().getName(), type.getName(), newParser.getClass().getName());
        }
    }

    /**
     * Take the String parameters from the message event and parse them into the required
     * format the commands method required to execute.
     *
     * @param event The message event to take parameters from.
     * @param metaCommand The method to imitate the fields of.
     * @return An Object[] array of all parameters parsed as required for the given method.
     * @throws IllegalArgumentException If one of the arguments could not be parsed in the required format.
     */

    public Object[] parseParameters(CommandEvent event, AbstractMetaCommand metaCommand) throws IllegalArgumentException {
        List<MetaParam> metaParams = metaCommand.getMetaParams();
        List<List<String>> inputs = event.getInput().getParameters();
        Object[] objects = new Object[metaParams.size()];

        int offset = 0;

        for (int i = 0; i < metaParams.size(); i++) {
            MetaParam param = metaParams.get(i);

            if (!param.isInput()) {
                objects[i] = event;
                offset++;
                continue;
            }

            List<String> input = inputs.get(i - offset);
            Object object = parseParam(event, param, input);

            if (object == null)
                return null;

            objects[i] = object;
        }

        return objects;
    }

    private Object parseParam(CommandEvent event, MetaParam param, List<String> items) throws IllegalArgumentException {
        Class<?> clazz = param.getParameter().getType();
        IParser parser = getParser(clazz);

        if (parser == null)
            throw new RuntimeException("No Parser was created for the data-type " + clazz.getName() + ".");

        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            Object objects = Array.newInstance(componentType, items.size());

            for (int i = 0; i < items.size(); i++) {
                Object o = parser.parse(event, componentType, items.get(i));

                if (componentType == boolean.class)
                    Array.setBoolean(objects, i, (boolean)o);
                else if (componentType == char.class)
                    Array.setChar(objects, i, (char)o);
                else if (componentType == double.class)
                    Array.setDouble(objects, i, (double)o);
                else if (componentType == float.class)
                    Array.setFloat(objects, i, (float)o);
                else if (componentType == long.class)
                    Array.setLong(objects, i, (long)o);
                else if (componentType == int.class)
                    Array.setInt(objects, i, (int)o);
                else if (componentType == short.class)
                    Array.setShort(objects, i, (short)o);
                else if (componentType == byte.class)
                    Array.setByte(objects, i, (byte)o);
                else
                    Array.set(objects, i, o);

            }
            return objects;
        }

        if (items.size() == 1)
            return parser.parse(event, clazz, items.get(0));

        commandler.getConfiler().getMisuseListener().unsupportedList(event, param, items);
        return null;
    }

    private IParser<?> getParser(Class<?> clazz) {
        if (clazz.isArray())
            clazz = clazz.getComponentType();

        if (parsers.containsKey(clazz))
            return parsers.get(clazz);

        for (Map.Entry<Class<?>, IParser<?>> entry : parsers.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz))
                return entry.getValue();
        }

        return null;
    }


    @Override
    public Iterator<IParser<?>> iterator() {
        return parsers.values().iterator();
    }
}
