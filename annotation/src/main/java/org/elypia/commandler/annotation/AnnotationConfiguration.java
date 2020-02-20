/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.annotation;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.elypia.commandler.utils.ReflectionUtils;
import org.slf4j.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Load data for Commandler through {@link Annotation}s.
 * This is the simplest way to use Commandler and keeps the metadata for
 * Modules, Commands, and Params closest to the functionality, however
 * can make code slighly messy.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public class AnnotationConfiguration extends AbstractConfiguration {

    /** SLF4J Logger */
    private static final Logger logger = LoggerFactory.getLogger(AnnotationConfiguration.class);

    private Collection<Class<?>> types;

    /**
     * Bypass searching and only add the specified classes.
     *
     * @param types The types to add.
     */
    public AnnotationConfiguration(Class<?>... types) {
        this(List.of(types));
    }

    /**
     * Bypass searching and only add the specified classes.
     *
     * @param types The types to add.
     */
    public AnnotationConfiguration(Collection<Class<?>> types) {
        this.types = types;
    }

    /**
     * Search the packages specified for classes and add those to the list
     * of classes to obtain data from.
     *
     * @param pkge The packages to search for classes in.
     */
    public AnnotationConfiguration(Package... pkge) {
        types = new ArrayList<>();

        for (Package p : pkge)
            types.addAll(ReflectionUtils.getClasses(p));
    }

    public AnnotationConfiguration(String... pkge) throws IOException {
        types = new ArrayList<>();

        for (String p : pkge)
            types.addAll(ReflectionUtils.getClasses(p));
    }

//    public void load() {
//
//        for (Class<?> type : types) {
//            if (type == CommandController.class)
//                context.addModules(getModule((Class<? extends CommandController>)type));
//            else if (type == ParamAdapter.class)
//                context.addAdapters(getAdapter((Class<? extends ParamAdapter>)type));
//            else if (type == ResponseBuilder.class)
//                context.addProviders(getResponseBuilder((Class<? extends ResponseBuilder<?, ?>>)type));
//            else
//                logger.warn("Found redundant being loaded in {}.", AnnotationConfiguration.class);
//        }
//
//        return context;
//    }
//
//    /**
//     * @param type The Java {@link Class} to parse..
//     * @return The {@link DataBuilder} that represents this {@link Commandler} module.
//     */
//    private ModuleBuilder getModule(Class<? extends CommandController> type) {
//        ModuleBuilder moduleBuilder = new ModuleBuilder(type);
//        Iterator<Annotation> annotations = List.of(type.getAnnotations()).iterator();
//
//        while (!annotations.hasNext()) {
//            Annotation annotation = annotations.next();
//            Class<? extends Annotation> annoType = annotation.annotationType();
//
//            if (annoType == Controller.class) {
//                Controller controller = (Controller)annotation;
//                moduleBuilder.setGroup(controller.group())
//                             .setHidden(controller.hidden());
//            } else
//                parseAnnotation(moduleBuilder, annotation, annoType);
//        }
//
//        for (Method method : type.getMethods()) {
//            if (method.isAnnotationPresent(Control.class))
//                moduleBuilder.addCommand(getCommand(method));
//        }
//
//        return moduleBuilder;
//    }
//
//    /**
//     * @param method The Java {@link Method} to parse..
//     * @return The {@link DataBuilder} that represents this {@link Commandler} command.
//     */
//    private CommandBuilder getCommand(Method method) {
//        CommandBuilder commandBuilder = new CommandBuilder(method);
//        Iterator<Annotation> annotations = List.of(method.getAnnotations()).iterator();
//
//        while (!annotations.hasNext()) {
//            Annotation annotation = annotations.next();
//            Class<? extends Annotation> annoType = annotation.annotationType();
//
//            if (annoType == Control.class) {
//                Control control = (Control)annotation;
//                commandBuilder.setHidden(control.hidden());
//            } else if (annoType == Default.class) {
//                commandBuilder.setDefault(true);
//            } else if (annoType == Static.class) {
//                commandBuilder.setStatic(true);
//            } else
//                parseAnnotation(commandBuilder, annotation, annoType);
//        }
//
//        for (Parameter parameter : method.getParameters()) {
//            if (method.isAnnotationPresent(Param.class))
//                commandBuilder.addParam(getParam(parameter));
//        }
//
//        return commandBuilder;
//    }
//
//    /**
//     * @param parameter The Java {@link Parameter} to parse..
//     * @return The {@link DataBuilder} that represents this {@link Commandler} param.
//     */
//    private ParamBuilder getParam(Parameter parameter) {
//        ParamBuilder paramBuilder = new ParamBuilder(parameter.getType(), parameter);
//        Iterator<Annotation> annotations = List.of(parameter.getAnnotations()).iterator();
//
//        while (!annotations.hasNext()) {
//            Annotation annotation = annotations.next();
//            Class<? extends Annotation> annoType = annotation.annotationType();
//
//            if (annoType == Param.class) {
//                Param param = (Param)annotation;
//                paramBuilder.setDefaultValue(AnnotationUtils.ifEffectivelyNull(param.defaultValue()));
//            } else
//                parseAnnotation(paramBuilder, annotation, annoType);
//        }
//
//        return paramBuilder;
//    }
//
//    /**
//     * Parse the generic annotations that all elements can have.
//     *
//     * @param builder The element that is being built.
//     * @param annotation The class, method, or parameter that's being parsed.
//     * @param type The type of annotation that's been used.
//     */
//    private <B extends ElementBuilder<B>> void parseAnnotation(B builder, Annotation annotation, Class<? extends Annotation> type) {
//        if (type == Help.class) {
//            Help help = (Help)annotation;
//            builder.setName(AnnotationUtils.ifEffectivelyNull(help.name()))
//                   .setHelp(AnnotationUtils.ifEffectivelyNull(help.help()));
//        } else if (type == Property.class) {
//            Property property = (Property)annotation;
//            builder.setProperty(property.key(), property.value());
//        } else if (type.isAnnotationPresent(PropertyWrapper.class)) {
//            PropertyWrapper wrapper = type.getAnnotation(PropertyWrapper.class);
//            String prefix = wrapper.type().getName() + ".";
//
//            for (Method method : type.getDeclaredMethods()) {
//                if (!method.isAnnotationPresent(Property.class))
//                    throw new IllegalStateException(PropertyWrapper.class + " property doesn't define the " + Property.class + " annotation.");
//
//                Property property = method.getAnnotation(Property.class);
//
//                try {
//                    builder.setProperty(prefix + property.key(), method.invoke(null).toString());
//                } catch (IllegalAccessException | InvocationTargetException ex) {
//                    logger.error("Failed to parse property for an Element.", ex);
//                }
//            }
//        }
//    }
//
//    /**
//     * @param type The type of {@link ParamAdapter} this is for.
//     * @return A {@link DataBuilder} for the {@link ParamAdapter} type.
//     */
//    private AdapterBuilder getAdapter(Class<? extends ParamAdapter> type) {
//        Objects.requireNonNull(type);
//
//        if (!type.isAnnotationPresent(Adapter.class)) {
//            logger.warn("Found {} but is not annotated with {}, ignoring.", ParamAdapter.class, Adapter.class);
//            return null;
//        }
//
//        return new AdapterBuilder(type).setCompatibleTypes(type.getAnnotation(Adapter.class).value());
//    }
//
//    /**
//     * @param type The type of {@link ResponseBuilder} this is for.
//     * @return A {@link DataBuilder} for the {@link ResponseBuilder} type.
//     */
//    private ProviderBuilder getResponseBuilder(Class<? extends ResponseBuilder<?, ?>> type) {
//        Objects.requireNonNull(type);
//
//        if (!type.isAnnotationPresent(Messenger.class)) {
//            logger.warn("Found {} but is not annotated with {}, ignoring.", ResponseBuilder.class, Messenger.class);
//            return null;
//        }
//
//        Messenger messenger = type.getAnnotation(Messenger.class);
//        return new ProviderBuilder(type)
//            .setBuildType(messenger.provides())
//            .setCompatibleTypes(messenger.value());
//    }

    @Override
    protected void addPropertyDirect(String key, Object value) {
        throw new IllegalStateException("Operating can not be performed on this configuration.");
    }

    @Override
    protected void clearPropertyDirect(String key) {
        throw new IllegalStateException("Operating can not be performed on this configuration.");
    }

    @Override
    protected Iterator<String> getKeysInternal() {
        return null;
    }

    @Override
    protected Object getPropertyInternal(String key) {
        return null;
    }

    @Override
    protected boolean isEmptyInternal() {
        return types.isEmpty();
    }

    @Override
    protected boolean containsKeyInternal(String key) {
        return false;
    }
}
