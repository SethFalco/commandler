package com.elypia.commandler.annotation.data;

import com.elypia.commandler.annotation.AnnotationUtils;
import com.elypia.commandler.annotation.properties.Aliases;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * @return The name of the property this is setting.
     */
    String key();

    /**
     * @return The key to set this property to, this should be
     * {@link AnnotationUtils#EFFECTIVELY_NULL} if this is for a Annotation wrapper
     * for a property such as {@link Aliases}.
     */
    String value() default AnnotationUtils.EFFECTIVELY_NULL;
}
