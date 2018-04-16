package com.elypia.commandler.annotations.filter;

import com.elypia.commandler.data.SearchScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Search {
    SearchScope scope() default SearchScope.GLOBAL;
}
