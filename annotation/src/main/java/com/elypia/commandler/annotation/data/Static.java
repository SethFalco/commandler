package com.elypia.commandler.annotation.data;

import com.elypia.commandler.annotation.properties.Aliases;
import com.elypia.commandler.dispatchers.StandardDispatcher;

import java.lang.annotation.*;

/**
 * A static commands is a commands which can be done globally,
 * for example the {@link StandardDispatcher} uses this property
 * to determine if a {@link Command} really needs the {@link Aliases alias}
 * of it's parent {@link Controller} before it can be performed.
 *
 * Different Dispatchers may choose to treat this differnetly.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Static {

}
