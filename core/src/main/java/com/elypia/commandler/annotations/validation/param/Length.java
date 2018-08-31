package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.IParamValidator;
import com.elypia.commandler.metadata.MetaParam;

import java.lang.annotation.*;
import java.util.function.Function;

/**
 * Set the minimum and maximum number of characters
 * an input {@link String} parameter may have.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Length {

    /**
     * The default minimum number of characters allowed. <br>
     * (Can't physically have less than this anyways.)
     */
    int DEFAULT_MIN = 0;

    /**
     * The default maximium number of characters allowed.
     * Normally this is the only value that needs changing.
     */
    int DEFAULT_MAX = Short.MAX_VALUE;

    /**
     * @return The minimum number of characters the {@link String} may have.
     */
    int min() default DEFAULT_MIN;

    /**
     * @return The maximum number of characters the {@link String} may have.
     */
    int max() default DEFAULT_MAX;

    class Validator extends IParamValidator<CommandEvent, String, Length> {

        public static final Function<Length, String> DEFAULT_HELP = (length) -> {
            StringBuilder builder = new StringBuilder("This parameter must be ");

            if (length.min() == Length.DEFAULT_MIN && length.max() != Length.DEFAULT_MAX)
                builder.append(String.format("less than %,d", length.max()));
            else if (length.min() != Length.DEFAULT_MIN && length.max() == Length.DEFAULT_MAX)
                builder.append(String.format("more than %,d", length.min()));
            else
                builder.append(String.format("between %,d and %,d", length.min(), length.max()));

            return builder.append(" characters long!").toString();
        };

        public Validator(Function<Length, String> help) {
            super(help);
        }

        @Override
        public boolean validate(CommandEvent event, String s, Length length, MetaParam param) {
            int l = s.length();
            return l >= length.min() && l <= length.max();
        }
    }

}
