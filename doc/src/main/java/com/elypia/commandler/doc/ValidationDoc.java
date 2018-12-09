package com.elypia.commandler.doc;

import java.lang.annotation.Annotation;

public class ValidationDoc<V extends Class<Annotation>> {

    /**
     * The annotation type this validator is for.
     */
    private V clazz;

    /**
     * The FontAwesome icon to use to represent this
     * icon; for example for settings one could use:
     * <code>fas fa-cog</code>.
     *
     * @see <a href="https://fontawesome.com/icons">FontAwesome</a>
     */
    private String icon;

    /**
     * Write the description template for this annotation
     * to display as guidance.
     */
    private String description;

    public V getClazz() {
        return clazz;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }
}
