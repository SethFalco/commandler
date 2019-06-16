package com.elypia.commandler.meta.loaders;

import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.meta.builder.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.*;

public class ReflectionLoader extends SearchingLoader {

    private static final Pattern nameMatcher = Pattern.compile(".+Module$");
    private static final Pattern pattern = Pattern.compile("[^a-z]+");

    public ReflectionLoader(Class<?> reference) {
        super(reference);
    }

    public ReflectionLoader(Package pkge) {
        super(pkge);
    }

    public ReflectionLoader(String pkge) throws IOException {
        super(pkge);
    }

    @Override
    public List<ModuleBuilder> getModules() {
        return types.stream()
            .filter((t) -> t.getSimpleName().endsWith("Module"))
            .map((t) -> {
                String typeName = t.getSimpleName();
                String moduleName = getReflectedName(typeName, "Module");

                return new ModuleBuilder((Class<? extends Handler>)t)
                    .setName(moduleName)
                    .setAliases(toAlias(moduleName))
                    .setHidden(typeName.endsWith("HiddenModule"));

            })
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<CommandBuilder> getCommands(Class<? extends Handler> type) {
        return Stream.of(type.getMethods())
            .filter((m) -> m.getName().endsWith("Command"))
            .map((m) -> {
                String methodName = m.getName();
                String commandName = getReflectedName(methodName, "Command");

                return new CommandBuilder(m)
                    .setName(commandName)
                    .setAliases(toAlias(commandName))
                    .setHidden(methodName.endsWith("HiddenCommand"))
                    .setDefault(methodName.endsWith("DefaultCommand"))
                    .setStatic(methodName.endsWith("StaticCommand"));
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ParamBuilder> getParams(Method method) {
        return Stream.of(method.getParameters())
            .map((p) -> new ParamBuilder(p.getType()).setName(p.getName()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<AdapterBuilder> getAdapters() {
        return null;
    }

    @Override
    public List<ProviderBuilder> getProviders() {
        return null;
    }

    /**
     * @param reflect The name of the reflected class/member.
     * @param type The type of meta this presents, for example "Module", or "Command".
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

        return builder.toString().trim();
    }

    private String toAlias(String name) {
        return pattern.matcher(name.toLowerCase()).replaceAll("");
    }
}
