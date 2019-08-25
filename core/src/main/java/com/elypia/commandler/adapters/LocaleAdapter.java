package com.elypia.commandler.adapters;

import com.elypia.commandler.api.Adapter;
import com.elypia.commandler.event.ActionEvent;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.utils.ChatUtils;
import org.slf4j.*;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class LocaleAdapter implements Adapter<Locale> {

    private static final Logger logger = LoggerFactory.getLogger(LocaleAdapter.class);

    private static final Locale[] LOCALES = Locale.getAvailableLocales();

    @Override
    public Locale adapt(String input, Class<? extends Locale> type, MetaParam metaParam, ActionEvent<?, ?> event) {
        for (Locale locale : LOCALES) {
            if (input.equalsIgnoreCase(locale.toLanguageTag()))
                return locale;

            if (!locale.getCountry().isBlank()) {
                String c = locale.getCountry();
                String dc = locale.getDisplayCountry(Locale.US);
                String dcl = locale.getDisplayCountry(locale);

                if (input.equalsIgnoreCase(c) || input.equalsIgnoreCase(dc) || input.equalsIgnoreCase(dcl))
                    return locale;

                if (input.equalsIgnoreCase(ChatUtils.replaceWithIndictors(c)))
                    return locale;

                try {
                    if (input.equalsIgnoreCase(locale.getISO3Country()))
                        return locale;
                } catch (MissingResourceException ex) {
                    // Do nothing, there is nothing wrong with this.
                }
            } else {
                String l = locale.getLanguage();
                String dl = locale.getDisplayLanguage(Locale.US);
                String dll = locale.getDisplayLanguage(locale);
                String lc = locale.getISO3Language();

                if (input.equalsIgnoreCase(l) || input.equalsIgnoreCase(dl) || input.equalsIgnoreCase(dll) || input.equalsIgnoreCase(lc))
                    return locale;
            }
        }

        return null;
    }
}
