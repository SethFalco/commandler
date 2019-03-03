package com.elypia.commandler.metadata;

import com.elypia.commandler.EventInput;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.exceptions.NoParserException;
import com.elypia.commandler.inject.ServiceProvider;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.data.*;
import org.slf4j.*;

import java.lang.reflect.Array;
import java.util.*;

/**
 * The {@link Param paramter} parser which is used to interpret
 * parameters specified in the {@link Command command} arguments.
 * Before a data-type can be used as an argument a {@link Parser parser}
 * must be specified and registered to the {@link ParameterParser}. <br>
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

    private final ServiceProvider provider;
    private MisuseListener misuse;

    protected Collection<ParserData> parsers;

    public ParameterParser(ServiceProvider provider, Collection<ParserData> parsers) {
        this.provider = provider;
        this.misuse = provider.get(MisuseListener.class);
        this.parsers = Objects.requireNonNull(parsers);

        for (ParserData parser : parsers)
            logger.debug("Parser added from class {}.", parser.getParserClass().getSimpleName());
    }

    /**
     * Take the String parameters from the message event and parse them into the required
     * format the commands method requires to execute.
     *
     * @param event The message event to take parameters from.
     * @return An Object[] array of all parameters parsed as required for the given method.
     */
    public Object[] processEvent(CommandlerEvent event) {
        EventInput input = event.getInput();
        Class<?>[] types = input.getCommandData().getMethod().getParameterTypes();
        Iterator<ParamData> params = input.getCommandData().getParams().iterator();
        Iterator<List<String>> inputs = input.getParameters().iterator();

        int length = types.length;
        Object[] objects = new Object[length];

        for (int i = 0; i < length; i++) {
            Class<?> type = types[i];

            if (CommandlerEvent.class.isAssignableFrom(type))
                objects[i] = event;

            else {
                List<String> meh = inputs.next();
                Object object = parseParameter(event, params.next(), meh);

                if (object == null)
                    return null;

                objects[i] = object;
            }
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
    protected Object parseParameter(CommandlerEvent event, ParamData param, List<String> items) {
        Class<?> type = param.getParameter().getType();
        Class<?> componentType = type.isArray() ? type.getComponentType() : type;
        Parser parser = getParser(componentType);

        if (parser == null)
            throw new RuntimeException(String.format("No parser was created for the data-type %s.", componentType.getName()));

        int size = items.size();

        if (type.isArray()) {
            Object output = Array.newInstance(componentType, size);

            for (int i = 0; i < size; i++) {
                String item = items.get(i);
                Object o = parser.parse(event, param, componentType, item);

                if (o == null) {
                    event.invalidate(misuse.onParamParseFailure(event, param, componentType, item));
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
            Object o = parser.parse(event, param, componentType, items.get(0));

            if (o == null)
                event.invalidate(misuse.onParamParseFailure(event, param, type, items.get(0)));

            return o;
        }

        event.invalidate(misuse.onListNotSupported(event, param, items));
        return null;
    }

    /**
     * Go through the registered parsers and get the one most appropriate
     * for this class, it will start by looking for one of the exact type
     * of this class but if one is not found it will look for a parser
     * by {@link Class#isAssignableFrom(Class)} instead. This will return
     * null if no {@link Parser} is found for this type.
     *
     * @param typeRequired The type that needs parsing.
     * @return The parser to can parse this data into this data-type.
     */
    private Parser getParser(Class typeRequired) {
        Optional<ParserData> parser = parsers.parallelStream()
            .filter((p) -> p.getCompatible().contains(typeRequired))
            .findAny();

        if (!parser.isPresent()) {
            parser = parsers.parallelStream()
                .filter((p) -> p.getCompatible().parallelStream()
                        .anyMatch((c) -> c.isAssignableFrom(typeRequired)))
                .findAny();
        }

        if (!parser.isPresent())
            throw new NoParserException("No parser implementation registered for data type %s.", typeRequired);

        return provider.get(parser.get().getParserClass());
    }
}
