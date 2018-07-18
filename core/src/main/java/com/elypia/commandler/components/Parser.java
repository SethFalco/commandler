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
 * must be specified and registered to the {@link Parser} via the
 * {@link Commandler#registerParser(Class, IParser)} method. <br>
 * The {@link Parser} is how {@link String} input provided
 * by the chat client is converted into the respective object and passed
 * to the method associated with the {@link Command}.
 */

public class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);
    private static final String REGISTERED_PARSER = "Registered the {} parser for the data-type {}.";
    private static final String REPLACED_PARSER = "The parser, {}, for the data-type {} has been replaced with {}.";

    protected Commandler commandler;
    protected IConfiler confiler;

    protected Map<Class<?>, IParser<?>> parsers;

    public Parser(Commandler commandler) {
        this.commandler = Objects.requireNonNull(commandler);
        confiler = commandler.getConfiler();
        parsers = new HashMap<>();

        registerParser(String.class, new StringParser());
        registerParser(Number.class, new NumberParser());
        registerParser(Boolean.class, new BooleanParser());
        registerParser(URL.class, new UrlParser());
        registerParser(Duration.class, new DurationParser());
        registerParser(Enum.class, new EnumParser());
    }

    public <T> void registerParser(Class<T> type, IParser<T> newParser) {
        IParser<?> oldParser = parsers.put(type, newParser);

        if (oldParser == null)
            logger.debug(REGISTERED_PARSER, newParser.getClass().getName(), type.getName());
        else
            logger.info(REPLACED_PARSER, oldParser.getClass().getName(), type.getName(), newParser.getClass().getName());
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

            objects[i] = parseParam(event, param, input);
        }

        return objects;
    }

    private Object parseParam(CommandEvent event, MetaParam param, List<String> items) throws IllegalArgumentException {
        Class<?> clazz = param.getParameter().getType();

        if (clazz.isArray())
            clazz = clazz.getComponentType();

        IParser parser = getParser(clazz);

        if (parser == null)
            throw new RuntimeException("No Parser was created for the data-type " + clazz.getName() + ".");

        if (clazz.isArray()) {
            Object[] objects = (Object[])Array.newInstance(clazz, items.size());

            for (int i = 0; i < items.size(); i++)
                objects[i] = clazz.cast(parser.parse(event, clazz, items.get(i)));

            return objects;
        }

        if (items.size() == 1)
            return clazz.cast(parser.parse(event, clazz, items.get(0)));

        commandler.getConfiler().getMisuseListener().unsupportedList(event, param, items);
        return null;
    }

    private IParser<?> getParser(Class<?> clazz) {
        if (parsers.containsKey(clazz))
            return parsers.get(clazz);

        for (Map.Entry<Class<?>, IParser<?>> entry : parsers.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz))
                return entry.getValue();
        }

        return null;
    }
}
