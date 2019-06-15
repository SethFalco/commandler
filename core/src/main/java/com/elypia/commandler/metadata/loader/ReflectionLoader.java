package com.elypia.commandler.metadata.loader;

import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.metadata.builder.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.*;

public class ReflectionLoader implements MetadataLoader {

    private static final Pattern nameMatcher = Pattern.compile(".+Module$");
    private static final Pattern pattern = Pattern.compile("[^a-z]+");

    @Override
    public List<Method> findCommands(Class<? extends Handler> clazz) {
        return Stream.of(clazz.getMethods())
            .filter((m) -> m.getName().endsWith("Command"))
            .collect(Collectors.toList());
    }

    @Override
    public ModuleBuilder loadModule(ModuleBuilder builder) {
        String name = builder.getClass().getSimpleName();

        if (!nameMatcher.matcher(name).matches())
            return builder;

        String module = getReflectedName(name, "Module");

        return builder
            .setName(module)
            .addAliases(toAlias(module))
            .setHidden(name.endsWith("HiddenModule"));
    }

    @Override
    public CommandBuilder loadCommand(CommandBuilder builder) {
        String command = getReflectedName(builder.getMethod().getName(), "Command");

        return builder
            .setName(command)
            .setAliases(toAlias(command))
            .setStatic(command.endsWith("StaticCommand"))
            .setDefault(command.endsWith("DefaultCommand"));
    }

    @Override
    public ParamBuilder loadParam(ParamBuilder builder) {
        return builder
            .setName(builder.getParameter().getName());
    }

    @Override
    public AdapterBuilder loadParser(AdapterBuilder builder) {
        return builder;
    }

    @Override
    public ProviderBuilder loadBuilder(ProviderBuilder builder) {
        return builder;
    }

    /**
     * @param reflect The name of the reflected class/member.
     * @param type The type of metadata this presents, for example "Module", or "Command".
     * @return The name to use for the command.
     */
    private String getReflectedName(String reflect, String type) {
        String trimmed = reflect.substring(0, reflect.length() - type.length());

        StringBuilder builder = new StringBuilder();

        for (char c : trimmed.toCharArray()) {
            if (c >= 'A' && c <= 'Z')
                builder.append(" ");

            builder.append(c);
        }

        return builder.toString();
    }

    private String toAlias(String name) {
        return pattern.matcher(name.toLowerCase()).replaceAll("");
    }
}
