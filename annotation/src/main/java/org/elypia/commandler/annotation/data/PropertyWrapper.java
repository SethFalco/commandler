package org.elypia.commandler.annotation.data;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyWrapper {

    /**
     * @return The type of object that owns and will use this property.
     * Type is seperated here so Commandler can internally call {@link Class#toString()}
     * which can improve compile time validation.
     */
    Class<?> type();
}
