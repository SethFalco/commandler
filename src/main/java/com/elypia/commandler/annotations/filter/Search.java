package com.elypia.commandler.annotations.filter;

import com.elypia.commandler.jda.data.SearchScope;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Search {
    SearchScope value() default SearchScope.GLOBAL;
}
