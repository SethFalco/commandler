package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.CommandInput;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.parsers.*;
import org.slf4j.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * The {@link Param paramter} parser which is used to interpret
 * parameters specified in the {@link Command command} arguments.
 * Before a data-type can be used as an argument a {@link IParser parser}
 * must be specified and registered to the {@link ParameterParser} via the
 * the {@link Parsers} annotation on {@link Handler handlers}. <br>
 * The {@link ParameterParser} is how {@link String} input provided
 * by the chat client is converted into the respective object and passed
 * to the method associated with the {@link Command}.
 */
public class ParameterParser {

    /**
     * We're using SLF4J to manage logging, remember to use a binding / implementation
     * and configure logging for when testing or running an application.
     */
    private static final Logger logger = LoggerFactory.getLogger(ParameterParser.class);

    private IMisuseHandler handler;

    /**
     * The registered parsers which allow us to use particular data-types
     * as {@link Command} {@link Param parameters}.
     */
    protected Map<Class<? extends IParser>, IParser> parsers;

    public ParameterParser(IMisuseHandler handler) {
        this.handler = Objects.requireNonNull(handler);
        parsers = new HashMap<>();

        add(
            BooleanParser.class,
            CharacterParser.class,
            DurationParser.class,
            EnumParser.class,
            NumberParser.class,
            StringParser.class,
            UrlParser.class
        );
    }

    /**
     * Register a new {@link IParser}. This will allow {@link Commandler} to
     * parse {@link String} input from the {@link CommandInput} provided
     * by the user into the respective arguments required by the {@link Command}. <br>
     * {@link Commandler} may register a few {@link IParser parsers} by default for
     * common types and primitives.
     *
     * @param types The type of parsers to register.
     *              These will be initalizes lazily as required.
     */
    @SafeVarargs
    final public void add(Class<? extends IParser>... types) {
        for (var type : types)
            parsers.put(type, null);
    }

    /**
     * Take the String parameters from the message event and parse them into the required
     * format the commands method requires to execute.
     *
     * @param event The message event to take parameters from.
     * @param commandData The method to imitate the fields of.
     * @return An Object[] array of all parameters parsed as required for the given method.
     */
    public Object[] processEvent(ICommandEvent<?, ?, ?> event, CommandData commandData) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
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
    protected Object parseParameter(ICommandEvent<?, ?, ?> event, ParamData param, List<String> items) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> type = param.getParameter().getType();
        Class<?> componentType = type.isArray() ? type.getComponentType() : type;
        IParser parser = getParser(componentType);

        if (parser == null)
            throw new RuntimeException(String.format("No parser was created for the data-type %s.", componentType.getName()));

        int size = items.size();

        if (type.isArray()) {
            Object output = Array.newInstance(componentType, size);

            for (int i = 0; i < size; i++) {
                String item = items.get(i);
                Object o = parser.parse(event, componentType, item);

                if (o == null) {
                    event.invalidate(handler.onParamParseFailure(event, param, componentType, item));
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
                event.invalidate(handler.onParamParseFailure(event, param, type, items.get(0)));

            return o;
        }

        event.invalidate(handler.onListNotSupported(event, param, items));
        return null;
    }

    /**
     * Go through the registered parsers and get the one most appropriate
     * for this class, it will start by looking for one of the exact type
     * of this class but if one is not found it will look for a parser
     * by {@link Class#isAssignableFrom(Class)} instead. This will return
     * null if no {@link IParser} is found for this type.
     *
     * @param typeRequired The type that needs parsing.
     * @return The parser to can parse this data into this data-type.
     */
    private IParser<?, ?> getParser(Class<?> typeRequired) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (var parser : parsers.entrySet()) {
            Class<? extends IParser> parserType = parser.getKey();
            Class<?>[] compatibleTypes = parserType.getAnnotation(Compatible.class).value();

            for (Class<?> type : compatibleTypes) {
                if (type == typeRequired) {
                    var value = parser.getValue();

                    if (value == null)
                        parser.setValue(parserType.getConstructor().newInstance());

                    return value;
                }
            }
        }

        return null;
    }
}
