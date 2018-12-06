package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.parsers.*;
import org.slf4j.*;

import java.lang.reflect.Array;
import java.net.URL;
import java.time.Duration;
import java.util.*;

/**
 * The {@link Param paramter} parser which is used to interpret
 * parameters specified in the {@link Command command} arguments.
 * Before a data-type can be used as an argument a {@link IParser parser}
 * must be specified and registered to the {@link ParseRegister} via the
 * {@link Commandler#registerParser(IParser, Class[])} method. <br>
 * The {@link ParseRegister} is how {@link String} input provided
 * by the chat client is converted into the respective object and passed
 * to the method associated with the {@link Command}.
 */
public class ParseRegister implements Iterable<IParser> {

    /**
     * This is logged whenever a existing {@link IParser} for a
     * particular data-type is replaced with a new one.
     */
    private static final String REPLACED_PARSER = "The parser, {}, for the data-type {} has been replaced with {}.";

    /**
     * This is thrown whenever a data-type is required however
     * this {@link ParseRegister} doesn't have an {@link IParser}
     * registered to know how to parse it.
     */
    private static final String PARSER_NOT_FOUND = "No parser was created for the data-type %s.";

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */
    private static final Logger logger = LoggerFactory.getLogger(ParseRegister.class);

    /**
     * Our parent Commandler instance which receives and manages the events.
     */
    protected Commandler commandler;

    /**
     * The registered parsers which allow us to use particular data-types
     * as {@link Command} {@link Param parameters}.
     */
    protected Map<Class<?>, IParser> parsers;

    public ParseRegister(Commandler commandler) {
        this.commandler = Objects.requireNonNull(commandler);
        parsers = new HashMap<>();

        // ? Register default parsers provided by Commandler
        registerParser(new BooleanParser(), BooleanParser.TYPES);
        registerParser(new CharacterParser(), CharacterParser.TYPES);
        registerParser(new DurationParser(), Duration.class);
        registerParser(new EnumParser(), Enum.class);
        registerParser(new NumberParser(), NumberParser.TYPES);
        registerParser(new StringParser(), String.class);
        registerParser(new UrlParser(), URL.class);
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
            IParser oldParser = parsers.put(type, newParser);

            if (oldParser != null) {
                String oldName = oldParser.getClass().getName();
                String newName = newParser.getClass().getName();

                logger.info(REPLACED_PARSER, oldName, type.getName(), newName);
            }
        }
    }

    /**
     * Take the String parameters from the message event and parse them into the required
     * format the commands method requires to execute.
     *
     * @param event The message event to take parameters from.
     * @param commandData The method to imitate the fields of.
     * @return An Object[] array of all parameters parsed as required for the given method.
     */
    public Object[] processEvent(ICommandEvent<?, ?, ?> event, CommandData commandData) {
        List<ParamData> paramData = commandData.getParamData();
        List<List<String>> inputs = event.getInput().getParameters();

        int size = paramData.size();
        Object[] objects = new Object[size];

        int offset = 0;

        for (int i = 0; i < size; i++) {
            ParamData param = paramData.get(i);

            if (!param.isInput()) {
                objects[i] = event;
                offset++;
                continue;
            }

            List<String> input = inputs.get(i - offset);
            Object object = parseParameter(event, param, input);

            if (object == null)
                return null;

            objects[i] = object;
        }

        return objects;
    }

    /**
     * This actually convertes an individual parameter into the type
     * required for a command. If the type required is an array,
     * we convert each item in the array using the
     * {@link Class#getComponentType() component type}. <br>
     * This should return null if a parameter fails to parse or list
     * parameter is provided where lists are not supported.
     *
     * @param event The message event to take parameters from.
     * @param param The static parameter data associated with the parameter.
     * @param items The input provided by the user, this will only contain
     *              more than one item if the user provided a list of items.
     * @return      The parsed object as required for the command, or null
     *              if we failed to parse the input. (Usually user misuse.)
     */
    protected Object parseParameter(ICommandEvent<?, ?, ?> event, ParamData param, List<String> items) {
        Class<?> type = param.getParameter().getType();
        Class<?> componentType = type.isArray() ? type.getComponentType() : type;
        IParser parser = getParser(componentType);

        if (parser == null)
            throw new RuntimeException(String.format(PARSER_NOT_FOUND, componentType.getName()));

        int size = items.size();

        if (type.isArray()) {
            Object output = Array.newInstance(componentType, size);

            for (int i = 0; i < size; i++) {
                String item = items.get(i);
                Object o = parser.parse(event, componentType, item);

                if (o == null) {
                    event.invalidate(commandler.getMisuseListener().onParamParseFailure(event, param, componentType, item));
                    return null;
                }

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
            Object o = parser.parse(event, componentType, items.get(0));

            if (o == null)
                event.invalidate(commandler.getMisuseListener().onParamParseFailure(event, param, type, items.get(0)));

            return o;
        }

        event.invalidate(commandler.getMisuseListener().onListNotSupported(event, param, items));
        return null;
    }

    /**
     * Go through the registered parsers and get the one most appropriate
     * for this class, it will start by looking for one of the exact type
     * of this class but if one is not found it will look for a parser
     * by {@link Class#isAssignableFrom(Class)} instead. This will return
     * null if no {@link IParser} is found for this type.
     *
     * @param clazz The class to obtain a parser for.
     * @return The parser to can parse this data into this data-type.
     */
    protected IParser<?, ?> getParser(Class<?> clazz) {
        if (parsers.containsKey(clazz))
            return parsers.get(clazz);

        for (Map.Entry<Class<?>, IParser> entry : parsers.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz))
                return entry.getValue();
        }

        return null;
    }

    @Override
    public Iterator<IParser> iterator() {
        return parsers.values().iterator();
    }
}
