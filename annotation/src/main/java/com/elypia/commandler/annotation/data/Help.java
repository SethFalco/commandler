package com.elypia.commandler.annotation.data;

import com.elypia.commandler.annotation.AnnotationUtils;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Help {

    /** The display name of this item. */
    String name() default AnnotationUtils.EFFECTIVELY_NULL;

    /** A small description of what this does or contains. */
    String description() default AnnotationUtils.EFFECTIVELY_NULL;
}
