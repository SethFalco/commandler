package com.elypia.commandler.providers;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.ResponseProvider;

import javax.inject.Singleton;
import java.net.URL;

/**
 * A provider for types that can make desireable output from
 * just the {@link Object#toString()} method.
 */
@Singleton
@Compatible({String.class, Character.class, char.class, Boolean.class, boolean.class, URL.class})
public class MiscToStringProvider implements ResponseProvider<Object, String> {

    @Override
    public String provide(Object output) {
        return output.toString();
    }
}
