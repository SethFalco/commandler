package com.elypia.commandler.providers;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Provider;
import com.elypia.commandler.interfaces.ResponseProvider;

import javax.inject.Singleton;
import java.net.URL;

/**
 * A provider for types that can make desireable output from
 * just the {@link Object#toString()} method.
 */
@Singleton
@Provider(provides = String.class, value = {String.class, Character.class, char.class, Boolean.class, boolean.class, URL.class})
public class MiscToStringProvider implements ResponseProvider<Object, String> {

    @Override
    public String provide(CommandlerEvent<?, String> event, Object output) {
        return output.toString();
    }
}
