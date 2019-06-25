package com.elypia.commandler.dispatchers;

import com.elypia.commandler.*;
import com.elypia.commandler.exceptions.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.data.*;
import com.elypia.commandler.utils.CommandlerUtils;
import org.slf4j.*;

import java.util.*;
import java.util.regex.*;

/**
 * The default implementation of the CommandDispatcher, this implementation
 * heavily relies on regular expression to match content and map the tokens
 * to command input. This assumes a standard formatted command:<br>
 * <code>{prefix}{module} {command} {params}</code>
 * With exceptional circumstances.
 */
public class CommandDispatcher implements Dispatcher {

    /** SLF4J Logger */
    private static final Logger logger = LoggerFactory.getLogger(CommandDispatcher.class);

    /**
     * This matches every argument in the commands,
     * any comma seperated <strong>args</strong> will be split by {@link #itemsPattern} as a list.
     */
    private static final Pattern paramsPattern = Pattern.compile("(?:(?:\"(?:\\\\\"|[^\"])*\"|[^\\s,]+)(?:\\s*,\\s*)?)+");

    /**
     * The default item regex, this matches every item within a list of parameters.
     * This is for list parameters as a single parameter can contain multiple items.
     */
    private static final Pattern itemsPattern = Pattern.compile("(?<!\\\\)\"(?<quote>.+?)(?<!\\\\)\"|(?<word>[^\\s,]+)");

    private final Commandler commandler;
    private final Context context;
    private final String[] prefixes;

    public CommandDispatcher(Commandler commandler) {
        this(commandler, (String[])null);
    }

    public CommandDispatcher(Commandler commandler, String... prefixes) {
        this.commandler = commandler;
        this.context = commandler.getContext();
        this.prefixes = prefixes;
    }

    @Override
    public Object dispatch(Controller controller, Object source, String content) {
        Object response;

        try {
            CommandlerEvent<?> event = parse(controller, source, content);
            Input input = event.getInput();
            MetaModule module = input.getModule();
            Handler handler = commandler.getInjector().getInstance(module.getHandlerType());
            Object[] params = commandler.getAdapterManager().adaptEvent(event);
            commandler.getValidationManager().validate(event, handler, params);

            if (commandler.getTestManager().isFailing(handler))
                throw new ModuleDisabledException(input);

            MetaCommand command = input.getCommand();
            response = command.getMethod().invoke(handler, params);
        } catch (Exception ex) {
            response = commandler.getMisuseManager().route(ex);
        }

        Object object;

        if (response == null)
            return null;

        object = commandler.getProviderManager().provide(controller, response);
        return object;
    }

    @Override
    public boolean isValid(Object source, String content) {
        Collection<String> prefixes = getPrefixes(source);

        if (prefixes == null)
            return true;

        for (String prefix : prefixes) {
            if (content.startsWith(prefix))
                return true;
        }

        return false;
    }

    @Override
    public CommandlerEvent parse(Controller controller, Object source, String content) throws OnlyPrefixException, NoDefaultCommandException, ModuleNotFoundException, ParamCountMismatchException {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(source);
        Objects.requireNonNull(content);

        if (!isValid(source, content))
            return null;

        Collection<String> prefixes = getPrefixes(source);

        if (prefixes != null) {
            Optional<String> optPrefix = prefixes.stream()
                .filter(content::startsWith)
                .findAny();

            String prefix = optPrefix.orElseThrow(
                () -> new IllegalStateException("This command isn't valid, but passed the valid check. (Prefix is defined but command doesn't start with it.)")
            );

            content = content.substring(prefix.length());
        }

        String[] command = CommandlerUtils.splitSpaces(content);

        if (command.length == 0)
            throw new OnlyPrefixException();

        String first = command[0];
        MetaModule usedModule = null;
        MetaCommand usedCommand = null;
        String params = null;

        for (MetaModule metaModule : context.getModules()) {
            if (metaModule.performed(first)) {
                usedModule = metaModule;

                if (command.length > 1) {
                    for (MetaCommand metaCommand : metaModule.getCommands()) {
                        if (metaCommand.performed(command[1])) {
                            usedCommand = metaCommand;
                            break;
                        }
                    }

                    if (usedCommand == null)
                        throw new RuntimeException("Found module but command doesn't exist.");

                    params = content
                        .replace(command[0], "")
                        .replace(command[1], "")
                        .trim();
                } else {
                    MetaCommand data = metaModule.getDefaultCommand();

                    if (data != null)
                        usedCommand = data;
                    else
                        throw new NoDefaultCommandException(metaModule);

                    params = content
                        .replace(command[0], "")
                        .trim();
                }

                break;
            }

            for (MetaCommand metaCommand : metaModule.getStaticCommands()) {
                if (metaCommand.performed(command[0])) {
                    usedModule = metaModule;
                    usedCommand = metaCommand;
                }

                params = content
                    .replace(command[0], "")
                    .trim();
            }
        }

        if (usedModule == null)
            throw new ModuleNotFoundException();

        List<List<String>> parameters = new ArrayList<>();

        if (!params.isBlank()) {
            Matcher paramMatcher = paramsPattern.matcher(params);

            while (paramMatcher.find()) {
                String group = paramMatcher.group();
                Matcher splitMatcher = itemsPattern.matcher(group);

                List<String> list = new ArrayList<>();
                while (splitMatcher.find()) {
                    String quote = splitMatcher.group("quote");
                    list.add(quote != null ? quote : splitMatcher.group("word"));
                }

                parameters.add(list);
            }
        }

        Input input = new Input(content, usedModule, usedCommand, parameters);

        if (!usedCommand.isValidParamCount(parameters.size()))
            throw new ParamCountMismatchException(input);

        return new CommandlerEvent<>(controller, source, input);
    }

    /**
     * @param event The event that this occcured.
     * @return A list of all prefixes.
     */
    private Collection<String> getPrefixes(Object event) {
        if (prefixes == null)
            return null;

        return List.of(prefixes);
    }
}
