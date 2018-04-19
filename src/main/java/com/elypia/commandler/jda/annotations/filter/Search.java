package com.elypia.commandler.jda.annotations.filter;

import com.elypia.commandler.jda.data.SearchScope;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Search {
    SearchScope value() default SearchScope.GLOBAL;
}
