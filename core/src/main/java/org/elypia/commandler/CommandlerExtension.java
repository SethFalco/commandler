package org.elypia.commandler;

import org.apache.commons.lang3.ClassUtils;
import org.elypia.commandler.annotation.*;
import org.elypia.commandler.annotation.stereotypes.*;
import org.elypia.commandler.api.*;
import org.elypia.commandler.groups.Miscellaneous;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * CDI extention for Java CDI. This seeks and registeres all beans
 * to make Commandler function. All of Commandler's functionalities
 * are bound to the CDI container and the beans picked up by this extension.
 *
 * @author seth@elypia.org
 * @since 4.0.0
 */
public class CommandlerExtension implements Extension {

    private static final Logger logger = LoggerFactory.getLogger(CommandlerExtension.class);

    private Collection<Class<? extends Dispatcher>> dispatcherTypes;
    private Collection<Class<? extends HeaderBinder>> headerBinderTypes;
    private Collection<MetaAdapter> metaAdapters;
    private Collection<MetaMessenger> metaMessengers;
    private Collection<MetaController> metaControllers;

    public CommandlerExtension() {
        dispatcherTypes = new ArrayList<>();
        headerBinderTypes = new ArrayList<>();
        metaAdapters = new ArrayList<>();
        metaMessengers = new ArrayList<>();
        metaControllers = new ArrayList<>();
    }

    public void processBean(@Observes final ProcessBean<?> processBean) throws IllegalAccessException, InvocationTargetException {
        if (!(processBean.getAnnotated() instanceof AnnotatedType))
            return;

        AnnotatedType<?> annotatedType = (AnnotatedType)processBean.getAnnotated();
        Class<?> javaClazz = annotatedType.getJavaClass();

        if (javaClazz.isAnnotationPresent(MessageDispatcher.class))
            dispatcherTypes.add((Class<? extends Dispatcher>)javaClazz);

        if (javaClazz.isAnnotationPresent(Binder.class))
            headerBinderTypes.add((Class<? extends HeaderBinder>)javaClazz);

        if (javaClazz.isAnnotationPresent(ParamAdapter.class)) {
            ParamAdapter paramAdapter = javaClazz.getAnnotation(ParamAdapter.class);
            Class<?>[] values = paramAdapter.value();
            Collection<Class<?>> allCompatibleTypes = getCompatibleTypes(values);

            MetaAdapter metaAdapter = new MetaAdapter((Class<? extends Adapter>)javaClazz, allCompatibleTypes);
            metaAdapters.add(metaAdapter);
        }

        if (javaClazz.isAnnotationPresent(MessageProvider.class)) {
            MessageProvider messageProvider = javaClazz.getAnnotation(MessageProvider.class);
            Class<?>[] values = messageProvider.value();
            Collection<Class<?>> allCompatibleTypes = getCompatibleTypes(values);

            MetaMessenger metaMessenger = new MetaMessenger((Class<? extends Messenger>)javaClazz, messageProvider.provides(), allCompatibleTypes);
            metaMessengers.add(metaMessenger);
        }

        boolean isController = Stream.of(javaClazz.getAnnotations())
            .anyMatch((annotation) -> annotation.annotationType().isAnnotationPresent(Controller.class));

        if (isController || javaClazz.isAnnotationPresent(Controller.class)) {
            String group = null;
            boolean isHidden = false;

            if (javaClazz.isAnnotationPresent(Controller.class)) {
                Controller controller = javaClazz.getAnnotation(Controller.class);
                isHidden = controller.hidden();
            }

            if (javaClazz.isAnnotationPresent(Group.class)) {
                group = javaClazz.getAnnotation(Group.class).message();
            } else if (Stream.of(javaClazz.getAnnotations()).anyMatch((annotation) -> annotation.annotationType().isAnnotationPresent(Group.class))) {
                Annotation annotation = Stream.of(javaClazz.getAnnotations())
                    .filter((anno) -> anno.annotationType().isAnnotationPresent(Group.class))
                    .limit(1)
                    .findAny()
                    .get();

                String message = annotation.annotationType().getAnnotation(Group.class).message();

                if (AnnotationUtils.isEffectivelyNull(message))
                    group = "{" + annotation.annotationType().getName() + "}";
                else
                    group = message;
            } // Make this check package too, going up recursively

            ComponentConfig component = convertComponent(javaClazz, 0);
            List<MetaCommand> commands = convertCommands(javaClazz);

            if (group == null || AnnotationUtils.isEffectivelyNull(group))
                group = "{" + Miscellaneous.class.getName() + "}";

            metaControllers.add(new MetaController(javaClazz, group, component.name, component.description, isHidden, component.properties, commands));
        }
    }

    private List<MetaCommand> convertCommands(Class<?> type) throws IllegalAccessException, InvocationTargetException {
        List<MetaCommand> commands = new ArrayList<>();
        Method[] methods = type.getMethods();

        for (Method method : methods) {
            boolean isCommand = Stream.of(method.getAnnotations())
                .anyMatch((annotation) -> annotation.annotationType().isAnnotationPresent(Command.class));

            if (!isCommand && !method.isAnnotationPresent(Command.class))
                continue;

            ComponentConfig component = convertComponent(method, 0);

            boolean isHidden = false;
            List<MetaParam> params = convertParams(method);

            if (method.isAnnotationPresent(Command.class))
                isHidden = method.getAnnotation(Command.class).hidden();

            commands.add(new MetaCommand(method, component.name, component.description, isHidden, component.properties, params));
        }

        return commands;
    }

    private List<MetaParam> convertParams(Method method) throws IllegalAccessException, InvocationTargetException {
        List<MetaParam> params = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        int index = 0;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (!parameter.isAnnotationPresent(Param.class))
                continue;

            Param value = parameter.getAnnotation(Param.class);

            ComponentConfig component = convertComponent(parameter, index);

            String defaultValue = value.value();
            String defaultValueDisplay = value.displayAs();

            if (AnnotationUtils.isEffectivelyNull(defaultValue))
                defaultValue = null;

            if (AnnotationUtils.isEffectivelyNull(defaultValueDisplay))
                defaultValueDisplay = defaultValue;

            params.add(new MetaParam(i, index++, parameter, component.name, component.description, defaultValue, defaultValueDisplay, component.properties));
        }

        return params;
    }

    private ComponentConfig convertComponent(AnnotatedElement element, int index) throws IllegalAccessException, InvocationTargetException {
        String name = null;
        String description = null;
        Map<String, MetaProperty> properties = convertProperties(element, index);

        if (element.isAnnotationPresent(Help.class)) {
            Help help = element.getAnnotation(Help.class);
            name = help.name();
            description = help.description();
        }

        if (element instanceof Class) {
            Class<?> clazz = (Class<?>)element;
            String clazzName = clazz.getName();

            if (name == null)
                name = "{" + clazzName + ".name}";
            if (description == null)
                description = "{" + clazzName + ".description}";
        } else if (element instanceof Method) {
            Method method = (Method)element;
            String methodName = method.getName();
            Class<?> clazz = method.getDeclaringClass();
            String clazzName = clazz.getName();

            if (name == null)
                name = "{" + clazzName + "-" + methodName + ".name}";
            if (description == null)
                description = "{" + clazzName + "-" + methodName + ".description}";
        } else if (element instanceof Parameter) {
            Parameter parameter = (Parameter)element;
            String parameterName = String.valueOf(index);
            Method method = (Method)parameter.getDeclaringExecutable();
            String methodName = method.getName();
            Class<?> clazz = method.getDeclaringClass();
            String clazzName = clazz.getName();

            if (name == null)
                name = "{" + clazzName + "-" + methodName + "-" + parameterName + ".name}";
            if (description == null)
                description = "{" + clazzName + "-" + methodName + "-" + parameterName + ".description}";
        }

        return new ComponentConfig(name, description, properties);
    }

    private Map<String, MetaProperty> convertProperties(AnnotatedElement element, int index) throws IllegalAccessException, InvocationTargetException {
        Map<String, MetaProperty> properties = new HashMap<>();

        for (Annotation annotation : element.getAnnotations()) {
            if (annotation instanceof Property) {
                Property ap = (Property)annotation;
                MetaProperty property = new MetaProperty(ap.key(), ap.value(), ap.isPublic(), ap.i18n(), ap.displayName());
                properties.put(ap.key(), property);
                continue;
            }

            Class<? extends Annotation> annotationClazz = annotation.annotationType();

            if (annotationClazz.isAnnotationPresent(PropertyWrapper.class)) {
                PropertyWrapper propertyWrapper = annotationClazz.getAnnotation(PropertyWrapper.class);
                Class<?> type = propertyWrapper.type();

                Method[] methods = annotationClazz.getMethods();

                for (Method annotationMethod : methods) {
                    if (!annotationMethod.isAnnotationPresent(Property.class))
                        continue;

                    Property fp = annotationMethod.getAnnotation(Property.class);
                    String value = annotationMethod.invoke(annotation).toString();

                    if (fp.i18n() && value.equals(AnnotationUtils.EFFECTIVELY_NULL)) {
                        if (element instanceof Class) {
                            Class<?> clazz = (Class<?>)element;
                            String clazzName = clazz.getName();
                            value = "{" +  clazzName + "-" + type.getName() + "." + fp.key() + "}";
                        } else if (element instanceof Method) {
                            Method method = (Method)element;
                            String methodName = method.getName();
                            Class<?> clazz = method.getDeclaringClass();
                            String clazzName = clazz.getName();
                            value = "{" +  clazzName + "-" + methodName + "-" + type.getName() + "." + fp.key() + "}";
                        } else if (element instanceof Parameter) {
                            Parameter parameter = (Parameter)element;
                            String parameterName = String.valueOf(index);
                            Method method = (Method)parameter.getDeclaringExecutable();
                            String methodName = method.getName();
                            Class<?> clazz = method.getDeclaringClass();
                            String clazzName = clazz.getName();
                            value = "{" +  clazzName + "-" + methodName + "-" + parameterName + "-" + type.getName() + "." + fp.key() + "}";
                        }
                    }

                    MetaProperty property = new MetaProperty(fp.key(), value, fp.isPublic(), fp.i18n(), fp.displayName());
                    properties.put(type.getName() + "." + fp.key(), property);
                }
            }
        }

        return properties;
    }

    private Collection<Class<?>> getCompatibleTypes(Class<?>[] types) {
        Collection<Class<?>> allTypes = new HashSet<>();

        for (Class<?> type : types) {
            Class<?> primitive = ClassUtils.wrapperToPrimitive(type);

            if (primitive != null)
                allTypes.add(primitive);

            allTypes.add(type);
        }

        return allTypes;
    }

    public Collection<Class<? extends Dispatcher>> getDispatcherTypes() {
        return Collections.unmodifiableCollection(dispatcherTypes);
    }

    public Collection<Class<? extends HeaderBinder>> getHeaderBinders() {
        return Collections.unmodifiableCollection(headerBinderTypes);
    }

    public Collection<MetaAdapter> getMetaAdapters() {
        return Collections.unmodifiableCollection(metaAdapters);
    }

    public Collection<MetaMessenger> getMetaMessengers() {
        return Collections.unmodifiableCollection(metaMessengers);
    }

    public Collection<MetaController> getMetaControllers() {
        return Collections.unmodifiableCollection(metaControllers);
    }

    /**
     * Private object to convert abstract data and put it in
     * the implementations.
     *
     * @author seth@elypia.org (Seth Falco)
     */
    private static class ComponentConfig {

        private String name;
        private String description;
        private Map<String, MetaProperty> properties;

        private ComponentConfig(String name, String description, Map<String, MetaProperty> properties) {
            this.name = name;
            this.description = description;
            this.properties = properties;
        }
    }
}
