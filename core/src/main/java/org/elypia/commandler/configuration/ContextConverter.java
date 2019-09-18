package org.elypia.commandler.configuration;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.elypia.commandler.Commandler;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.metadata.*;

import javax.inject.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Convert the relevent parts of the {@link CommandlerConfiguration}
 * to the {@link org.elypia.commandler.Context} in order to contextualize
 * the metadata for the various components in {@link Commandler}.
 */
@Singleton
public class ContextConverter {

    private CommandlerConfiguration config;

    @Inject
    public ContextConverter(CommandlerConfiguration config) {
        this.config = config;
    }

    public List<MetaController> convert() {
        List<MetaController> metaControllers = new ArrayList<>();

        List<ImmutableHierarchicalConfiguration> controllers = config.getConfiguration().immutableConfigurationsAt("commandler.controller");

        for (ImmutableHierarchicalConfiguration controller : controllers) {
            ComponentConfig component = convertComponent(controller);
            Class<?> type;

            try {
                type = Class.forName(controller.getString("type"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            String group = controller.getString("group");
            boolean hidden = controller.getBoolean("hidden", false);
            List<MetaCommand> commands = convertCommands(type, controller);
            metaControllers.add(new MetaController((Class<? extends Controller>)type, group, component.name, component.description, hidden, component.properties, commands));
        }

        return metaControllers;
    }

    private List<MetaCommand> convertCommands(Class<?> type, ImmutableHierarchicalConfiguration controller) {
        List<ImmutableHierarchicalConfiguration> commandsConfig = controller.immutableConfigurationsAt("command");

        List<MetaCommand> metaCommands = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration command : commandsConfig) {
            ComponentConfig component = convertComponent(command);
            Method method = Arrays.stream(type.getMethods()).filter(m -> m.getName().equals(command.getString("method"))).findAny().orElseThrow();
            boolean hidden = command.getBoolean("hidden", false);
            boolean isStatic = command.getBoolean("static", false);
            boolean isDefault = command.getBoolean("default", false);
            List<MetaParam> params = convertParams(method, command);
            metaCommands.add(new MetaCommand(method, component.name, component.description, hidden, isStatic, isDefault, component.properties, params));
        }

        return metaCommands;
    }

    private List<MetaParam> convertParams(Method method, ImmutableHierarchicalConfiguration command) {
        List<ImmutableHierarchicalConfiguration> paramsConfig = command.immutableConfigurationsAt("param");

        List<MetaParam> metaParams = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration param : paramsConfig) {
            ComponentConfig component = convertComponent(command);
            String defaultValue = param.getString("defaultValue");
            metaParams.add(new MetaParam(String.class, method.getParameters()[0], component.name, component.description, defaultValue, component.properties));
        }

        return metaParams;
    }

    private ComponentConfig convertComponent(ImmutableHierarchicalConfiguration component) {
        String name = component.getString("name");
        String description = component.getString("description");
        Properties properties = convertProperties(component);

        return new ComponentConfig(name, description, properties);
    }

    private Properties convertProperties(ImmutableHierarchicalConfiguration component) {
        List<ImmutableHierarchicalConfiguration> propertiesConfig = component.immutableConfigurationsAt("property");

        Properties properties = new Properties();

        for (ImmutableHierarchicalConfiguration property : propertiesConfig) {
            String key = property.getString("key");
            String value = property.getString("value");
            properties.put(key, value);
        }

        return properties;
    }

    private class ComponentConfig {

        private String name;
        private String description;
        private Properties properties;

        private ComponentConfig(String name, String description, Properties properties) {
            this.name = name;
            this.description = description;
            this.properties = properties;
        }
    }
}
