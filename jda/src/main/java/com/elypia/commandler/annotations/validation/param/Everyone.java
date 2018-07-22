package com.elypia.commandler.annotations.validation.param;

import com.elypia.commandler.annotations.validation.Validation;

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
@Validation
public @interface Everyone {

}
