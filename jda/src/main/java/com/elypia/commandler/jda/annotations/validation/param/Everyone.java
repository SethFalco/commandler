package com.elypia.commandler.jda.annotations.validation.param;

import java.lang.annotation.*;

/**
 * The @Everyone annotations monitors the parameter
 * to ensure it does contain and @everyone or @here. <br>
 * This is useful if taking user input as it would be
 * unfavourable to allow users that don't have the @everyone
 * permission to be able to perform it by making the bot @everyone
 * on their behalf.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Everyone {

}
